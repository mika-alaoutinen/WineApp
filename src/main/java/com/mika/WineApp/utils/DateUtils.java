package com.mika.WineApp.utils;

import com.mika.WineApp.errors.InvalidDateException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public interface DateUtils {

    static List<LocalDate> parseMonthRange(List<String> dates) throws InvalidDateException {
        if (dates == null) {
            return null;
        }

        if (dates.size() != 2) {
            throw new InvalidDateException(dates);
        }

        var startDate = parseYearMonthString(dates.get(0)).atDay(1);
        var endDate = parseYearMonthString(dates.get(1)).atEndOfMonth();
        return List.of(startDate, endDate);
    }

    private static YearMonth parseYearMonthString(String date) {
        try {
            return YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }
    }
}
