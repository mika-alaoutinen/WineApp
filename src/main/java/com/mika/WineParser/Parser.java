package com.mika.WineParser;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/** Driver class for TextParser. Gets Wines and Reviews as lists. */
@Slf4j
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
        String textsDirectory = new ClassPathResource("texts")
                .getFile()
                .getPath();

        log.error("texts directory: " + textsDirectory);

        // Parse all wines and reviews:
        textParser.parseAll(textsDirectory);

        // Parsed wines and reviews:
        this.wines = textParser.getWines();
        this.reviews = textParser.getReviews();

        log.error("wines: " + wines);
        log.error("reviews: " + reviews);
    }

    public List<Wine> getWines() {
        return wines;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
