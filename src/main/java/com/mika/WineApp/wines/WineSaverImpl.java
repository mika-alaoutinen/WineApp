package com.mika.WineApp.wines;

import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.infra.db.WineSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
class WineSaverImpl implements WineSaver {
    private final WineRepository repository;

    @Override
    public boolean isEmpty() {
        return repository.count() == 0;
    }

    @Override
    public void saveAll(Collection<Wine> wines) {
        repository.saveAll(wines);
    }
}
