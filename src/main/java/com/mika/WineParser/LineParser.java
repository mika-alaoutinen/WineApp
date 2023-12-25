package com.mika.WineParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

interface LineParser {

    /**
     * Parses String into LocalDate.
     *
     * @param line with date information
     * @return LocalDate
     */
    static LocalDate date(String line) {
        String[] words = line.split(" ");
        String date = words[1];

        var formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        var formatter2 = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate localDate;

        try {
            localDate = LocalDate.parse(date, formatter1);
        } catch (DateTimeParseException e) {
            localDate = LocalDate.parse(date, formatter2);
        }

        return localDate;
    }

    /**
     * Parses a list of keywords. Keywords are scrubbed of extra white space and converted into lowercase.
     * If a keyword contains a full stop, it is removed.
     *
     * @param line parsed line.
     * @return keywords as List<String>.
     */
    static List<String> keywords(String line) {
        String[] keywords = stringContent(line).split(",");

        return Arrays.stream(keywords)
                .map(String::strip)
                .map(String::toLowerCase)
                .map(word -> word.replace(".", ""))
                .filter(word -> !word.isBlank() && word.length() > 2)
                .toList();
    }

    /**
     * Removes the first word on a line, which identifies what information that line contains.
     * For example 'Kuvaus: puolimakea, hapokas...'.
     *
     * @param line parsed line.
     * @return parsed content as a String[].
     */
    static String[] removeIdentifierWord(String line) {
        String[] words = line.strip().split(" ");
        return Arrays.copyOfRange(words, 1, words.length);
    }

    /**
     * Parses line and returns its content as a String.
     *
     * @param line parsed line.
     * @return parsed content as a String.
     */
    static String stringContent(String line) {
        String[] name = removeIdentifierWord(line);
        return String.join(" ", name);
    }

}
