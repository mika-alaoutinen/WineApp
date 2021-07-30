package com.mika.WineApp.utils;

import com.mika.WineApp.errors.invaliddate.InvalidDateException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class DateUtils {

    public static LocalDate[] parseMonthRange(String[] dates) throws InvalidDateException {
        if (dates == null || dates.length != 2) {
            throw new InvalidDateException(dates);
        }

        LocalDate[] parsedDates = new LocalDate[2];
        String date = dates[0];

        try {
            parsedDates[0] = YearMonth
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM"))
                    .atDay(1);

            date = dates[1];

            parsedDates[1] = YearMonth
                    .parse(date, DateTimeFormatter.ofPattern("yyyy-MM"))
                    .atEndOfMonth();

        } catch (DateTimeParseException e) {
            throw new InvalidDateException(date);
        }

        return parsedDates;
    }
}
