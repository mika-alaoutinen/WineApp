package com.mika.WineApp.TestUtilities;

import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

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
}