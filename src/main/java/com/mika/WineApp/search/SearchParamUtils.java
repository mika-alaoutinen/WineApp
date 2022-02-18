package com.mika.WineApp.search;

import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

interface SearchParamUtils {

    static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    static <T> Optional<Pair<T, T>> createPair(List<T> list) {
        return isValidList(list)
               ? Optional.of(Pair.of(list.get(0), list.get(1)))
               : Optional.empty();
    }

    private static boolean isValidList(List<?> list) {
        return list != null && list.size() == 2;
    }
}
