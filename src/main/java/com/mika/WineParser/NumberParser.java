package com.mika.WineParser;

interface NumberParser {

    /**
     * Parses String and checks that it's a valid number.
     *
     * @param s String.
     * @return price as double or -1 if price is not a valid number.
     */
    static double doubleOrDefault(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
