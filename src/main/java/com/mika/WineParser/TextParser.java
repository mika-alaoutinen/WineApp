package com.mika.WineParser;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import com.mika.WineApp.models.WineType;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextParser {
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

    private List<Wine> wines;
    private List<Review> reviews;

    public TextParser() {
        initAttributes();
        this.wines = new ArrayList<>();
        this.reviews = new ArrayList<>();
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

// Getters:

    public List<Wine> getWines() {
        return wines;
    }

    public List<Review> getReviews() {
        return reviews;
    }

// Methods for parsing text:

    /**
     * Parses all files in given directory.
     * @param pathToDirectory file directory as String.
     * @throws IOException e
     */
    public void parseAll(String pathToDirectory) throws IOException {
        // Wine types in order of text files:
        List<WineType> types = List.of(
                WineType.SPARKLING,
                WineType.OTHER,
                WineType.RED,
                WineType.ROSE,
                WineType.WHITE
        );

        // Get a list of Paths to where the text files are:
        try (Stream<Path> paths = Files.walk(Path.of(pathToDirectory))) {
            List<Path> pathList = paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            // Parse each file:
            for (int i = 0; i < pathList.size(); i++) {
                parse(pathList.get(i), types.get(i));
            }
        }
    }

    /**
     * The main method for parsing text files. Uses a Scanner to scan all
     * lines in a file and then handles each line one by one.
     * @param path to text file.
     * @param wineType type, f. ex. RED or WHITE.
     * @throws IOException exception.
     */
    private void parse(Path path, WineType wineType) throws IOException {
        Scanner scanner = new Scanner(path);
        String line = scanner.nextLine();

        // Parse each line:
        while (scanner.hasNextLine()) {
            // Parse wine name:
            if (line.contains("VIINI:")) {
                name = parseStringContent(line);

            // Parse review date:
            } else if (line.contains("Päivämäärä:")) {
                date = parseDate(line);

            // Parse wine's country:
            } else if (line.contains("Maa:")) {
                country = parseStringContent(line);

            // Parse wine's price and quantity
            } else if (line.contains("Hinta:")) {
                String[] content = removeIdentifierWord(line);
                price = parseDouble(content[0]);
                String quantityStr = content[2].substring(1);
                quantity = parseDouble(quantityStr);

            // Parse wine's description. Note that description can contain multiple lines!
            } else if (line.contains("Kuvaus:")) {
                description = parseKeywords(line);

                // If next word is not "SopiiNautittavaksi:", keep parsing description:
                while (!scanner.hasNext("SopiiNautittavaksi:")) {
                    line = scanner.nextLine();
                    if (line.toLowerCase().contains("huom:")) {
                        break;
                    }

                    description.addAll(parseKeywords(line));
                }

            // Parse recommended food pairings for the wine:
            } else if (line.contains("SopiiNautittavaksi:")) {
                foodPairings = parseKeywords(line);

            // Parse URL for wine. If URL is blank, set URL to "null":
            } else if (line.contains("url")) {
                // TODO: validate URL
//                String urlStr = parseStringContent(line);
//                url = validateUrl(urlStr);
                url = parseStringContent(line);

            // Parse review texts from Mika or Salla:
            } else if (line.contains("Arvostelu")) {
                parseReview(line);

            // Parse review ratings from Mika or Salla:
            } else if (line.contains("Arvosana:")) {
                parseRating(line);
            }

            // Try to create new Wine and Review models and get the next line:
            createModels(wineType);
            line = scanner.nextLine();
        }
    }

// Utility methods for parsing text:

    /**
     * Removes the first word on a line, which identifies what information that line contains.
     * For example 'Kuvaus: puolimakea, hapokas...'.
     * @param line parsed line.
     * @return parsed content as a String[].
     */
    private String[] removeIdentifierWord(String line) {
        String[] words = line.strip().split(" ");
        return Arrays.copyOfRange(words, 1, words.length);
    }

    /**
     * Parses line and returns it's content as a String.
     * @param line parsed line.
     * @return parsed content as a String.
     */
    private String parseStringContent(String line) {
        String[] name = removeIdentifierWord(line);
        return String.join(" ", name);
    }

    /**
     * Parses a list of keywords. Keywords are scrubbed of extra white space and converted into lowercase.
     * If a keyword contains a full stop, it is removed.
     * @param line parsed line.
     * @return keywords as List<String>.
     */
    private List<String> parseKeywords(String line) {
        String[] keywords = parseStringContent(line).split(",");

        return Arrays.stream(keywords)
                .map(String::strip)
                .map(String::toLowerCase)
                .map(word -> word.replace(".", ""))
                .filter(word -> !word.isBlank() && word.length() > 2)
                .collect(Collectors.toList());
    }

    /**
     * Parses String into LocalDate.
     * @param line with date information
     * @return LocalDate
     */
    private LocalDate parseDate(String line) {
        String[] words = line.split(" ");
        String date = words[1];

        var formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        var formatter2 = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate localDate;

        try {
            localDate = LocalDate.parse(date, formatter1);
        } catch (DateTimeParseException e) {
            localDate = LocalDate.parse(date, formatter2);
        }
        return localDate;
    }

    /**
     * Validates a given URL address.
     * @param url as a String.
     * @return URL as a String if URL is valid, else returns "null".
     * @throws IOException exception.
     */
    private String validateUrl(String url) throws IOException {
        if (url.isBlank()) {
            return "";
        } else if (!isUrlValid(url)) {
            return "vanhentunut";
        }
        return url;
    }

    /**
     * Checks if a website returns a 200 OK response.
     * @param urlStr URL address as a String.
     * @return true if response from site is 200, else return false.
     * @throws IOException exception.
     */
    private boolean isUrlValid(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.connect();

        return connection.getResponseCode() == 200;
    }

    /**
     * Parses line and sets content as review text for Mika or Salla.
     * @param line parsed line.
     */
    private void parseReview(String line) {
        if (line.contains("ArvosteluMika:")) {
            reviewTextMika = parseStringContent(line);
        } else if (line.contains("ArvosteluSalla:")) {
            reviewTextSalla = parseStringContent(line);
        } else {
            System.out.println("line: " + line);
        }
    }

    /**
     * Parses the rating on scale of 0-5 and assigns who gave the rating.
     * @param line parsed line.
     */
    private void parseRating(String line) {
        String[] words = removeIdentifierWord(line);

        if (words.length < 2) {
            System.out.println("Error on line: " + line);
        // Ratings from either Mika or Salla:
        } else if (words.length == 2) {
            if (words[1].equals("M")) {
                ratingMika = parseDouble(words[0]);
            } else {
                ratingSalla = parseDouble(words[0]);
            }
        // Ratings from both Mika and Salla, both gave the same rating:
        } else if (words.length == 3) {
            ratingMika = parseDouble(words[0]);
            ratingSalla= parseDouble(words[0]);
        // Ratings from both Mika and Salla, different ratings:
        } else if (words.length == 4) {
            if (words[1].equals("M")) {
                ratingMika = parseDouble(words[0]);
                ratingSalla = parseDouble(words[2]);
            } else if (words[1].equals("S")) {
                ratingMika = parseDouble(words[2]);
                ratingSalla = parseDouble(words[0]);
            } else {
                System.out.println("line: " + line);
            }
        } else {
            System.out.println("line: " + line);
        }
    }

    /**
     * Parses String and checks that it's a valid number.
     * @param s String.
     * @return price as double or -1 if price is not a valid number.
     */
    private double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            // return -1
        }
        return -1;
    }

    /**
     * If all parameters have been parsed, creates a new Wine and a new Review.
     * @param wineType type of wine, e.g. red or white
     */
    private void createModels(WineType wineType) {
        // Haven't parsed all lines yet, keep parsing.
        if (ratingMika == -1 && ratingSalla == -1) {
            return;
        }

        // Create new Wine:
        Wine newWine = new Wine(name, wineType, country, price, quantity, description, foodPairings, url);
        wines.add(newWine);

        // Create new Reviews and add them to Wine model:
        if (!reviewTextMika.isEmpty()) {
            reviews.add(new Review("Mika", date, reviewTextMika, ratingMika, newWine));
        }

        if (!reviewTextSalla.isEmpty()) {
            reviews.add(new Review("Salla", date, reviewTextSalla, ratingSalla, newWine));
        }

        // Initiate attribute values again for next entry in file:
        initAttributes();
    }
}
