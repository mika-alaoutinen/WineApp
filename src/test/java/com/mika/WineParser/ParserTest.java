package com.mika.WineParser;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.entities.WineType;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @Test
    void parses_valid_wines() {
        var parser = new Parser();
        var winesByType = parser.getWines()
                .stream()
                .collect(Collectors.groupingBy(Wine::getType));

        assertEquals(2, winesByType.get(WineType.SPARKLING).size());
        assertEquals(1, winesByType.get(WineType.OTHER).size());
        assertEquals(2, winesByType.get(WineType.RED).size());
        assertEquals(2, winesByType.get(WineType.ROSE).size());
        assertEquals(4, winesByType.get(WineType.WHITE).size());
    }

    @Test
    void parses_valid_reviews() {
        var parser = new Parser();
        var reviewsByAuthor = parser.getReviews()
                .stream()
                .collect(Collectors.groupingBy(Review::getAuthor));

        assertEquals(9, reviewsByAuthor.get("Mika").size());
        assertEquals(10, reviewsByAuthor.get("Salla").size());
    }

}
