package com.mika.WineApp.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mika.WineApp.errors.invaliddate.InvalidDateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class DateUtilsTest {

    @Test
    void shouldParseValidDates() {
        LocalDate[] expectedDates = {
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 2, 28)
        };

        String[] dates = { "2021-01", "2021-02" };
        assertDatesAreEqual(expectedDates, DateUtils.parseMonthRange(dates));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("createInvalidDates")
    void shouldThrowExceptionWhenNotGivenTwoDates(String[] dates) {
        assertThrows(InvalidDateException.class, () -> DateUtils.parseMonthRange(dates));
    }

    private static Stream<Arguments> createInvalidDates() {
        return Stream.of(
                Arguments.of((Object) new String[] { "2020-01" }),
                Arguments.of((Object) new String[] { "2020-01", "2020-02", "2020-03" })
        );
    }

    private void assertDatesAreEqual(LocalDate[] expected, LocalDate[] parsed) {
        IntStream
                .range(0, parsed.length)
                .forEach(i -> assertEquals(expected[i], parsed[i]));
    }
}
