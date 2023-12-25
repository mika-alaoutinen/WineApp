package com.mika.WineParser;

interface RatingParser {

    /**
     * Parses the rating on scale of 0-5 and assigns who gave the rating.
     *
     * @param line parsed line.
     * @return array of [ratingMika, ratingSalla]
     */
    static double[] ratings(String line) {
        double ratingMika = -1;
        double ratingSalla = -1;

        String[] words = LineParser.removeIdentifierWord(line);

        if (words.length < 2) {
            System.out.println("Error on line: " + line);
        } else if (words.length == 2) { // Ratings from either Mika or Salla:
            if (words[1].equals("M")) {
                ratingMika = NumberParser.doubleOrDefault(words[0]);
            } else {
                ratingSalla = NumberParser.doubleOrDefault(words[0]);
            }
        } else if (words.length == 3) { // Ratings from both Mika and Salla, both gave the same rating:
            ratingMika = NumberParser.doubleOrDefault(words[0]);
            ratingSalla = NumberParser.doubleOrDefault(words[0]);
        } else if (words.length == 4) { // Ratings from both Mika and Salla, different ratings:
            if (words[1].equals("M")) {
                ratingMika = NumberParser.doubleOrDefault(words[0]);
                ratingSalla = NumberParser.doubleOrDefault(words[2]);
            } else if (words[1].equals("S")) {
                ratingMika = NumberParser.doubleOrDefault(words[2]);
                ratingSalla = NumberParser.doubleOrDefault(words[0]);
            } else {
                System.out.println("line: " + line);
            }
        } else {
            System.out.println("line: " + line);
        }

        return new double[]{ratingMika, ratingSalla};
    }
}
