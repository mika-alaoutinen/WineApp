package com.mika.WineApp.TestUtilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public interface TestUtilities {

    static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Needed to handle LocalDates
        return mapper;
    }

    static String getResponseString(MvcResult result) throws UnsupportedEncodingException {
        return result
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }
}