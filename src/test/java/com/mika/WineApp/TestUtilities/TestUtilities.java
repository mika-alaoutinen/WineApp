package com.mika.WineApp.TestUtilities;

import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Library containing common utility methods for test classes.
 */
public interface TestUtilities {

    /**
     * Turns MvcResult content into a string.
     *
     * @param result given by controller.
     * @return String
     * @throws UnsupportedEncodingException ex.
     */
    static String getResponseString(MvcResult result) throws UnsupportedEncodingException {
        return result
                .getResponse()
                .getContentAsString();
    }

    /**
     * Parses result from controller into a list of strings.
     * The result is turned into a string, which is then scrubbed of brackets and quotation marks.
     *
     * @param result given by controller.
     * @return List of words.
     * @throws UnsupportedEncodingException ex.
     */
    static List<String> parseWordsFromResponse(MvcResult result) throws UnsupportedEncodingException {
        String response = getResponseString(result);

        String[] words = response
                .substring(1, response.length() - 1)
                .replace("\"", "")
                .split(",");

        return List.of(words);
    }
}