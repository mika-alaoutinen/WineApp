package com.mika.WineApp.search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewSearchParamsTest {

    @Test
    void isEmptyWhenAllFieldsAreEmpty() {
        var emptyParams = ReviewSearchParams
                .builder()
                .author("")
                .dateRange(Collections.emptyList())
                .ratingRange(Collections.emptyList())
                .build();

        assertTrue(emptyParams.isEmpty());
    }

    @Test
    void notEmptyWhenAtLeastOneFieldExists() {
        var emptyParams = ReviewSearchParams
                .builder()
                .author("author")
                .build();

        assertFalse(emptyParams.isEmpty());
    }

    @Test
    void shouldWrapAuthorInOptional() {
        var searchParams = ReviewSearchParams
                .builder()
                .author("author")
                .build();

        assertTrue(searchParams
                .getAuthor()
                .isPresent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void authorShouldBeEmptyWhenNullOrBlank(String author) {
        var searchParams = ReviewSearchParams
                .builder()
                .author(author)
                .build();

        assertTrue(searchParams
                .getAuthor()
                .isEmpty());
    }

    @Test
    void shouldReturnDateRangeAsPair() {
        var searchParams = ReviewSearchParams
                .builder()
                .dateRange(List.of(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 2, 15)))
                .build();

        assertTrue(searchParams
                .getDateRange()
                .isPresent());
    }

    @ParameterizedTest
    @MethodSource("createInvalidDateRange")
    @NullAndEmptySource
    void shouldReturnEmptyWithInvalidDateRange(List<LocalDate> dateRange) {
        var searchParams = ReviewSearchParams
                .builder()
                .dateRange(dateRange)
                .build();

        assertTrue(searchParams
                .getDateRange()
                .isEmpty());
    }

    @Test
    void shouldReturnRatingRangeAsPair() {
        var searchParams = ReviewSearchParams
                .builder()
                .ratingRange(List.of(1.0, 5.0))
                .build();

        assertTrue(searchParams
                .getRatingRange()
                .isPresent());
    }

    @ParameterizedTest
    @MethodSource("createInvalidRatingRange")
    @NullAndEmptySource
    void shouldReturnEmptyWithInvalidRatingRange(List<LocalDate> dateRange) {
        var searchParams = ReviewSearchParams
                .builder()
                .dateRange(dateRange)
                .build();

        assertTrue(searchParams
                .getRatingRange()
                .isEmpty());
    }

    private static Stream<List<LocalDate>> createInvalidDateRange() {
        var jan = LocalDate.of(2022, 1, 1);
        var feb = LocalDate.of(2022, 2, 1);
        var mar = LocalDate.of(2022, 3, 1);
        return Stream.of(List.of(jan), List.of(jan, feb, mar));
    }

    private static Stream<List<Double>> createInvalidRatingRange() {
        return Stream.of(List.of(1.0), List.of(1.0, 2.0, 3.0));
    }
}
