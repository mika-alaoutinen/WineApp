package com.mika.WineApp.search;

import com.mika.WineApp.entities.WineType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WineTypeParserTest {
    private static final List<WineType> WINE_TYPES = List.of(WineType.values());

    @ParameterizedTest
    @ValueSource(strings = {"OTHER", "RED", "ROSE", "SPARKLING", "WHITE"})
    void shouldParseWineType(String wineType) {
        WineType type = WineTypeParser
                .parse(wineType)
                .get();
        assertTrue(WINE_TYPES.contains(type));
    }

    @ParameterizedTest
    @ValueSource(strings = {"other", "red", "rose", "sparkling", "white"})
    void shouldBeCaseInsensitive(String wineType) {
        WineType type = WineTypeParser
                .parse(wineType)
                .get();
        assertTrue(WINE_TYPES.contains(type));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "NOT WINE")
    void shouldReturnOptionalWhenInvalidWineType(String invalidType) {
        assertTrue(WineTypeParser
                .parse(invalidType)
                .isEmpty());
    }
}
