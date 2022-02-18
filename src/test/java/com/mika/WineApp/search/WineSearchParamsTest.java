package com.mika.WineApp.search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WineSearchParamsTest {

    @Test
    void isEmptyWhenAllFieldsAreEmpty() {
        var emptyParams = WineSearchParams
                .builder()
                .name("")
                .type("")
                .countries(Collections.emptyList())
                .volumes(Collections.emptyList())
                .priceRange(Collections.emptyList())
                .build();

        assertTrue(emptyParams.isEmpty());
    }

    @Test
    void notEmptyWhenAtLeastOneFieldExists() {
        var searchParams = WineSearchParams
                .builder()
                .name("name")
                .build();

        assertFalse(searchParams.isEmpty());
    }

    @Test
    void shouldWrapNameInOptional() {
        var searchParams = WineSearchParams
                .builder()
                .name("wine name")
                .build();

        assertTrue(searchParams
                .getName()
                .isPresent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void nameShouldBeEmptyWhenNullOrBlank(String name) {
        var searchParams = WineSearchParams
                .builder()
                .name(name)
                .build();

        assertTrue(searchParams
                .getName()
                .isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"OTHER", "RED", "ROSE", "SPARKLING", "WHITE"})
    void shouldWrapTypeInOptional(String type) {
        var searchParams = WineSearchParams
                .builder()
                .type(type)
                .build();

        assertTrue(searchParams
                .getType()
                .isPresent());
    }

    @Test
    void typeShouldBeEmptyWhenInvalid() {
        var searchParams = WineSearchParams
                .builder()
                .type("invalid")
                .build();

        assertTrue(searchParams
                .getType()
                .isEmpty());
    }

    @Test
    void shouldReturnValidCountries() {
        var searchParams = WineSearchParams
                .builder()
                .countries(List.of("Spain", "Italy"))
                .build();

        assertEquals(2, searchParams
                .getCountries()
                .size());
    }

    @Test
    void shouldRemoveBlankStringsFromCountries() {
        var searchParams = WineSearchParams
                .builder()
                .countries(List.of("Spain", ""))
                .build();

        assertEquals(1, searchParams
                .getCountries()
                .size());
    }

    @Test
    void shouldReturnEmptyListWithNullCountries() {
        var searchParams = WineSearchParams
                .builder()
                .countries(null)
                .build();

        assertTrue(searchParams
                .getCountries()
                .isEmpty());
    }

    @Test
    void shouldReturnVolumes() {
        var searchParams = WineSearchParams
                .builder()
                .volumes(List.of(1.0, 2.0))
                .build();

        assertEquals(2, searchParams
                .getVolumes()
                .size());
    }

    @Test
    void shouldReturnEmptyListWithNullVolumes() {
        var searchParams = WineSearchParams
                .builder()
                .volumes(null)
                .build();

        assertTrue(searchParams
                .getVolumes()
                .isEmpty());
    }

    @Test
    void shouldReturnPriceRangeAsPair() {
        var searchParams = WineSearchParams
                .builder()
                .priceRange(List.of(1.0, 10.0))
                .build();

        assertTrue(searchParams
                .getPriceRange()
                .isPresent());
    }

    @ParameterizedTest
    @MethodSource("createInvalidPriceRange")
    @NullAndEmptySource
    void shouldReturnEmptyOptionalWithInvalidPriceRange(List<Double> priceRange) {
        var searchParams = WineSearchParams
                .builder()
                .priceRange(priceRange)
                .build();

        assertTrue(searchParams
                .getPriceRange()
                .isEmpty());
    }

    private static Stream<List<Double>> createInvalidPriceRange() {
        return Stream.of(List.of(1.0), List.of(1.0, 2.0, 3.0));
    }
}
