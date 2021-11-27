package com.mika.WineApp.utils;

import com.mika.WineApp.errors.InvalidDateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void shouldParseValidDates() {
        List<LocalDate> expectedDates = List.of(
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 2, 28)
        );

        List<String> dates = List.of("2021-01", "2021-02");
        assertDatesAreEqual(expectedDates, DateUtils.parseMonthRange(dates));
    }

    @Test
    void shouldReturnNullWithNullInput() {
        assertNull(DateUtils.parseMonthRange(null));
    }

    @ParameterizedTest
    @MethodSource("createInvalidDates")
    void shouldThrowExceptionWhenNotGivenTwoDates(List<String> dates) {
        assertThrows(InvalidDateException.class, () -> DateUtils.parseMonthRange(dates));
    }

    private static Stream<Arguments> createInvalidDates() {
        return Stream.of(
                Arguments.of((Object) Collections.emptyList()),
                Arguments.of((Object) List.of("2020-01")),
                Arguments.of((Object) List.of("2020-01", "2020-02", "2020-03"))
        );
    }

    private void assertDatesAreEqual(List<LocalDate> expected, List<LocalDate> parsed) {
        IntStream
                .range(0, parsed.size())
                .forEach(i -> assertEquals(expected.get(i), parsed.get(i)));
    }
}
