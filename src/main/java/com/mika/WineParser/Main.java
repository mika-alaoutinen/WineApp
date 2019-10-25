package com.mika.WineParser;

import com.mika.WineApp.models.Review;
import com.mika.WineApp.models.Wine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/** Driver class for TextParser. Gets Wines and Reviews as lists. */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        TextParser parser = new TextParser();
        String textsDirectory = ResourceUtils.getFile("classpath:texts").getPath();

        // Parse all wines and reviews:
        try {
            parser.parseAll(textsDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parsed wines and reviews:
        List<Wine> wines = parser.getWines();
        List<Review> reviews = parser.getReviews();

        wines.forEach(w -> System.out.println(w.getName()));
    }
}
