package com.mika.WineApp.search;

import com.mika.WineApp.entities.WineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Builder
public class WineSearchParams {
    @Getter
    private final List<String> countries;
    @Getter
    private final List<Double> volumes;
    private final String name;
    private final String type;
    private final List<Double> priceRange;

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<WineType> getType() {
        return WineTypeParser.parse(type);
    }

    public Pair<Double, Double> getPriceRange() {
        if (priceRange.size() != 2) {
            throw new IllegalArgumentException("Could not construct Pair from list");
        }
        return Pair.of(priceRange.get(0), priceRange.get(1));
    }
}
