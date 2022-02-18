package com.mika.WineApp.search;

import com.mika.WineApp.entities.WineType;

import java.util.Optional;

interface WineTypeParser {
    static Optional<WineType> parse(String type) {
        try {
            return Optional
                    .ofNullable(type)
                    .map(String::toUpperCase)
                    .map(WineType::valueOf);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
