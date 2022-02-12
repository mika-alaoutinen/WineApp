package com.mika.WineApp.infra.db;

import com.mika.WineApp.entities.Wine;

import java.util.Collection;

public interface WineSaver {
    boolean isEmpty();

    void saveAll(Collection<Wine> wines);
}
