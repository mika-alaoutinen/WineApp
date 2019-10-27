package com.mika.WineParser;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.List;

/** Driver class for TextParser. Gets Wines and Reviews as lists. */
public class Parser {
    private List<Wine> wines;
    private List<Review> reviews;

    public Parser() {
        try { parse(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void parse() throws IOException {
        TextParser textParser = new TextParser();

        // Path to directory where the text files are:
        String textsDirectory = ResourceUtils
                .getFile("classpath:texts")
                .getPath();

        // Parse all wines and reviews:
        textParser.parseAll(textsDirectory);

        // Parsed wines and reviews:
        this.wines = textParser.getWines();
        this.reviews = textParser.getReviews();
    }

    public List<Wine> getWines() {
        return wines;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
