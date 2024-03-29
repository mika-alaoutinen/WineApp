package com.mika.WineParser;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.entities.WineType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

class TextParser {

    // Wine attributes:
    private String name;
    private String country;
    private double price;
    private double quantity;
    private List<String> description;
    private List<String> foodPairings;
    private String url;

    // Review attributes:
    private LocalDate date;
    private String reviewTextMika;
    private String reviewTextSalla;
    private double ratingMika;
    private double ratingSalla;

    @Getter
    private final List<Wine> wines = new ArrayList<>();

    @Getter
    private final List<Review> reviews = new ArrayList<>();

    TextParser() {
        initAttributes();
    }

    /**
     * Initializes attributes for Wines and Reviews. Values are reset after
     * a new Wine or Review is created.
     */
    private void initAttributes() {
        // Wine attributes:
        name = "";
        country = "";
        price = -1;
        quantity = -1;
        description = new ArrayList<>();
        foodPairings = new ArrayList<>();
        url = "";

        // Review attributes:
        date = null;
        reviewTextMika = "";
        reviewTextSalla = "";
        ratingMika = -1;
        ratingSalla = -1;
    }

    /**
     * The method for parsing text files. Receives the texts in
     * a Scanner and parses its contents line by line.
     *
     * @param scanner  containing text to be parsed.
     * @param wineType type, f. ex. RED or WHITE.
     */
    void parse(Scanner scanner, WineType wineType) {
        String line = scanner.nextLine();

        // Parse each line:
        while (scanner.hasNextLine()) {
            // Parse wine name:
            if (line.contains("VIINI:")) {
                name = LineParser.stringContent(line);

                // Parse review date:
            } else if (line.contains("Päivämäärä:")) {
                date = LineParser.date(line);

                // Parse wine's country:
            } else if (line.contains("Maa:")) {
                country = LineParser.stringContent(line);

                // Parse wine's price and quantity
            } else if (line.contains("Hinta:")) {
                String[] content = LineParser.removeIdentifierWord(line);
                price = NumberParser.doubleOrDefault(content[0]);
                String quantityStr = content[2].substring(1);
                quantity = NumberParser.doubleOrDefault(quantityStr);

                // Parse wine's description. Note that description can contain multiple lines!
            } else if (line.contains("Kuvaus:")) {
                description = LineParser.keywords(line);

                // If next word is not "SopiiNautittavaksi:", keep parsing description:
                while (!scanner.hasNext("SopiiNautittavaksi:")) {
                    line = scanner.nextLine();
                    if (line.toLowerCase().contains("huom:")) {
                        break;
                    }
                    description = Stream.concat(description.stream(), LineParser.keywords(line).stream()).toList();
                }

                // Parse recommended food pairings for the wine:
            } else if (line.contains("SopiiNautittavaksi:")) {
                foodPairings = LineParser.keywords(line);

                // Parse URL for wine. If URL is blank, set URL to "null":
            } else if (line.contains("url")) {
                url = LineParser.stringContent(line);

                // Parse review texts from Mika or Salla:
            } else if (line.contains("Arvostelu")) {
                parseReviewText(line);

                // Parse review ratings from Mika or Salla:
            } else if (line.contains("Arvosana:")) {
                var ratings = RatingParser.ratings(line);
                ratingMika = ratings[0];
                ratingSalla = ratings[1];
            }

            // Try to create new Wine and Review models and get the next line:
            createModels(wineType);
            line = scanner.nextLine();
        }
    }

    /**
     * Parses line and sets content as review text for Mika or Salla.
     *
     * @param line parsed line.
     */
    private void parseReviewText(String line) {
        if (line.contains("ArvosteluMika:")) {
            reviewTextMika = LineParser.stringContent(line);
        } else if (line.contains("ArvosteluSalla:")) {
            reviewTextSalla = LineParser.stringContent(line);
        } else {
            System.out.println("line: " + line);
        }
    }

    /**
     * If all parameters have been parsed, creates a new Wine and a new Review.
     *
     * @param wineType type of wine, e.g. red or white
     */
    private void createModels(WineType wineType) {
        // Haven't parsed all lines yet, keep parsing.
        if (ratingMika == -1 && ratingSalla == -1) {
            return;
        }

        Wine newWine = Wine
                .builder()
                .name(name)
                .type(wineType)
                .country(country)
                .price(price)
                .volume(quantity)
                .description(description)
                .foodPairings(foodPairings)
                .url(url)
                .reviews(Collections.emptyList())
                .build();
        wines.add(newWine);

        if (!reviewTextMika.isEmpty()) {
            reviews.add(Review
                    .builder()
                    .author("Mika")
                    .date(date)
                    .reviewText(reviewTextMika)
                    .rating(ratingMika)
                    .wine(newWine)
                    .build());
        }

        if (!reviewTextSalla.isEmpty()) {
            reviews.add(Review
                    .builder()
                    .author("Salla")
                    .date(date)
                    .reviewText(reviewTextSalla)
                    .rating(ratingSalla)
                    .wine(newWine)
                    .build());
        }

        // Initiate attribute values again for next entry in file:
        initAttributes();
    }
}
