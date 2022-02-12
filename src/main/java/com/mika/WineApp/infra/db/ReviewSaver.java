package com.mika.WineApp.infra.db;

import com.mika.WineApp.entities.Review;

import java.util.Collection;

public interface ReviewSaver {
    boolean isEmpty();

    void saveAll(Collection<Review> reviews);
}
