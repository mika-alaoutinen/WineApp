package com.mika.WineParser;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.entities.WineType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

/**
 * Driver class for TextParser. Gets Wines and Reviews as lists.
 */
@Getter
@Slf4j
public class Parser {

    private List<Wine> wines;
    private List<Review> reviews;

    public Parser() {
        try {
            parse();
        } catch (IOException e) {
            log.error("Failed to parse wine review text files.");
            log.error(e.getMessage());
        }
    }

    /**
     * Reads text files from resources and sends their contents to TextParser to parse.
     * Wraps the text in a Scanner object.
     *
     * @throws IOException ex
     */
    private void parse() throws IOException {
        var textParser = new TextParser();

        var wineTypes = List.of(
                WineType.SPARKLING,
                WineType.OTHER,
                WineType.RED,
                WineType.ROSE,
                WineType.WHITE
        );

        var files = List.of(
                "kuohuviinit.txt",
                "muut.txt",
                "punaviinit.txt",
                "roseviinit.txt",
                "valkoviinit.txt"
        );

        log.info("Attempting to parse the following files:");
        log.info(files + "\n");

        for (int i = 0; i < files.size(); i++) {
            var text = new DefaultResourceLoader()
                    .getResource("classpath:texts/" + files.get(i))
                    .getInputStream();

            log.info("Parsing wines: " + wineTypes.get(i));
            var reader = new BufferedReader(new InputStreamReader(text));
            textParser.parse(new Scanner(reader), wineTypes.get(i));
        }

        // Parsed wines and reviews:
        this.wines = textParser.getWines();
        this.reviews = textParser.getReviews();
    }
}
