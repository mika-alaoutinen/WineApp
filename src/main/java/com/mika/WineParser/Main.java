package com.mika.WineParser;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/** Driver class for TextParser. Gets Wines and Reviews as lists. */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        TextParser parser = new TextParser();

        // Path to directory where the text files are:
        String textsDirectory = ResourceUtils
                .getFile("classpath:texts")
                .getPath();

        // Parse all wines and reviews:
        try {
            parser.parseAll(textsDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parsed wines and reviews:
        List<Wine> wines = parser.getWines();
        List<Review> reviews = parser.getReviews();
    }
}
