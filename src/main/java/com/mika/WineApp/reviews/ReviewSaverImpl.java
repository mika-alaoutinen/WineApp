package com.mika.WineApp.reviews;

import com.mika.WineApp.entities.Review;
import com.mika.WineApp.infra.db.ReviewSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
class ReviewSaverImpl implements ReviewSaver {
    private final ReviewRepository repository;

    @Override
    public boolean isEmpty() {
        return repository.count() == 0;
    }

    @Override
    public void saveAll(Collection<Review> entities) {
        repository.saveAll(entities);
    }
}
