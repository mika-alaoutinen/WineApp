package com.mika.WineApp.utils;

import com.mika.WineApp.errors.invaliddate.InvalidDateException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public abstract class DateUtils {

    public static List<LocalDate> parseMonthRange(List<String> dates) throws InvalidDateException {
        if (dates == null) {
            return null;
        }

        if (dates.size() != 2) {
            throw new InvalidDateException(dates);
        }

        var startDate = parseYeahMonthString(dates.get(0)).atDay(1);
        var endDate = parseYeahMonthString(dates.get(1)).atEndOfMonth();
        return List.of(startDate, endDate);
    }

    private static YearMonth parseYeahMonthString(String date) {
        try {
            return YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }
    }
}
