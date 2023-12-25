package com.mika.WineParser;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.entities.WineType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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
    void valid_wine_has_full_info() {
        var parser = new Parser();
        var wine = parser.getWines().get(0);
        
        assertEquals("Espanja", wine.getCountry());
        assertEquals(
                List.of("erittäin kuiva", "keskihapokas", "päärynäinen", "persikkainen", "kypsän sitruksinen", "kevyen mausteinen"),
                wine.getDescription());
        assertEquals(
                List.of("seurustelujuomana", "pikkusuolaiset", "salaatit ja kasvisruoka", "kana ja kalkkuna"),
                wine.getFoodPairings());
        assertEquals(7.98, wine.getPrice());
        assertTrue(wine.getReviews().isEmpty());
        assertEquals(0.75, wine.getVolume());
        assertEquals(WineType.SPARKLING, wine.getType());
        assertEquals("http://www.alko.fi/tuotteet/53597/", wine.getUrl());
        assertNull(wine.getId());
        assertNull(wine.getUser());
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

    @Test
    void valid_review_has_full_info() {
        var parser = new Parser();

        var review1 = parser.getReviews().get(0);
        assertEquals("Salla", review1.getAuthor());
        assertEquals(LocalDate.of(2016, 5, 7), review1.getDate());
        assertEquals(4, review1.getRating());
        assertTrue(review1.getReviewText().contains("Esimerkiksi kanasalaatti voisi olla aika bueno"));
        assertEquals("Amalía Brut", review1.getWine().getName());
        assertNull(review1.getId());
        assertNull(review1.getUser());

        var review2 = parser.getReviews().get(1);
        assertEquals("Mika", review2.getAuthor());
        assertEquals(LocalDate.of(2017, 3, 2), review2.getDate());
        assertEquals(3, review2.getRating());
        assertTrue(review2.getReviewText().contains("Ensimaku on mieto ja pehmeän mansikkainen"));
        assertEquals("Castellblanc Rosado Medium Dry", review2.getWine().getName());
        assertNull(review2.getId());
        assertNull(review2.getUser());
    }

}
