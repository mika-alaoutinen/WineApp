package com.mika.WineApp.search;

import com.mika.WineApp.entities.WineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Builder
public class WineSearchParams {
    private final String name;
    private final String type;
    private final List<String> countries;
    private final List<Double> volumes;
    private final List<Double> priceRange;

    public boolean isEmpty() {
        boolean optionalsAreEmpty = Stream
                .of(getName(), getType(), getPriceRange())
                .allMatch(Optional::isEmpty);

        boolean listsAreEmpty = Stream
                .of(getCountries(), getVolumes())
                .allMatch(List::isEmpty);

        return optionalsAreEmpty && listsAreEmpty;
    }

    public Optional<String> getName() {
        return Optional
                .ofNullable(name)
                .filter(SearchParamUtils::notBlank);
    }

    public Optional<WineType> getType() {
        return WineTypeParser.parse(type);
    }

    public List<String> getCountries() {
        return countries == null
               ? Collections.emptyList()
               : countries
                       .stream()
                       .filter(SearchParamUtils::notBlank)
                       .toList();
    }

    public List<Double> getVolumes() {
        return volumes == null ? Collections.emptyList() : volumes;
    }

    public Optional<Pair<Double, Double>> getPriceRange() {
        return SearchParamUtils.createPair(priceRange);
    }
}
