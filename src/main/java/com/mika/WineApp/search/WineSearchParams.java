package com.mika.WineApp.search;

import com.mika.WineApp.entities.WineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Builder
public class WineSearchParams {
    private final String name;
    private final String type;
    private final List<String> countries;
    private final List<Double> volumes;
    private final List<Double> priceRange;

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<WineType> getType() {
        return WineTypeParser.parse(type);
    }

    public List<String> getCountries() {
        return countries
                .stream()
                .filter(country -> !country.isBlank())
                .toList();
    }

    public List<Double> getVolumes() {
        return volumes
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }

    public Optional<Pair<Double, Double>> getPriceRange() {
        return priceRange.size() == 2
               ? Optional.of(Pair.of(priceRange.get(0), priceRange.get(1)))
               : Optional.empty();
    }
}
