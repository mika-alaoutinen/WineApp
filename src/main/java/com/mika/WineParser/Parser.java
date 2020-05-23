package com.mika.WineParser;

import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.models.wine.WineType;
import lombok.Getter;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

/** Driver class for TextParser. Gets Wines and Reviews as lists. */
public class Parser {

    @Getter
    private List<Wine> wines;

    @Getter
    private List<Review> reviews;

    public Parser() {
        try { parse(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Reads text files from resources and sends their contents to TextParser to parse.
     * Wraps the text in a Scanner object.
     * @throws IOException ex
     */
    private void parse() throws IOException {
        TextParser textParser = new TextParser();

        var types = List.of(
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

        System.out.println("\nAttempting to parse the following files:");
        System.out.println(files + "\n");

        for (int i = 0; i < files.size(); i++) {
            InputStream text = new DefaultResourceLoader()
                    .getResource("classpath:texts/" + files.get(i))
                    .getInputStream();

            System.out.println("Parsing wines: " + types.get(i));
            var reader = new BufferedReader(new InputStreamReader(text));
            textParser.parse(new Scanner(reader), types.get(i));
        }

        // Parsed wines and reviews:
        this.wines = textParser.getWines();
        this.reviews = textParser.getReviews();
    }
}
