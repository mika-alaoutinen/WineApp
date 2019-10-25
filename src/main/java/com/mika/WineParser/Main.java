package com.mika.WineParser;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;

import java.io.IOException;
import java.util.List;

/** Driver class for TextParser. Gets Wines and Reviews as lists. */
public class Main {
    public static void main(String[] args) {
        TextParser parser = new TextParser();

        // Important: set the path to the directory where the text files are here!
        String textsDirectory = "texts";

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
