package com.mika.WineApp.search;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchParamUtilsTest {

    @Test
    void shouldNotBeBlank() {
        assertTrue(SearchParamUtils.notBlank("abc"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldBeBlank(String input) {
        assertFalse(SearchParamUtils.notBlank(input));
    }

    @Test
    void shouldCreatePair() {
        var pair = SearchParamUtils.createPair(List.of(1, 2));
        assertTrue(pair.isPresent());
    }

    @ParameterizedTest
    @MethodSource("createInvalidPair")
    void shouldNotCreatePairWithInvalidInput(List<Integer> numbers) {
        var pair = SearchParamUtils.createPair(numbers);
        assertTrue(pair.isEmpty());
    }

    private static Stream<List<Integer>> createInvalidPair() {
        return Stream.of(
                List.of(1),
                List.of(1, 2, 3)
        );
    }
}
