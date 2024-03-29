package com.mika.WineApp.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Builder
public class ReviewSearchParams {
    private final String author;
    private final List<LocalDate> dateRange;
    private final List<Double> ratingRange;

    public boolean isEmpty() {
        return Stream
                .of(getAuthor(), getDateRange(), getRatingRange())
                .allMatch(Optional::isEmpty);
    }

    public Optional<String> getAuthor() {
        return Optional
                .ofNullable(author)
                .filter(SearchParamUtils::notBlank);
    }

    public Optional<Pair<LocalDate, LocalDate>> getDateRange() {
        return SearchParamUtils.createPair(dateRange);
    }

    public Optional<Pair<Double, Double>> getRatingRange() {
        return SearchParamUtils.createPair(ratingRange);
    }
}
