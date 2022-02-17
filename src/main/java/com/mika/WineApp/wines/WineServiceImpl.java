package com.mika.WineApp.wines;

import com.mika.WineApp.authentication.UserAuthentication;
import com.mika.WineApp.entities.Wine;
import com.mika.WineApp.errors.BadRequestException;
import com.mika.WineApp.errors.ForbiddenException;
import com.mika.WineApp.search.WineSearchParams;
import com.mika.WineApp.search.WineSpecification;
import com.mika.WineApp.services.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class WineServiceImpl implements WineService {
    private final WineRepository repository;
    private final UserAuthentication userAuth;

    // --- CRUD methods ---
    @Override
    public List<Wine> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Override
    public Optional<Wine> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Wine add(Wine newWine) {
        if (!isValid(newWine.getName())) {
            throw new BadRequestException(newWine, newWine.getName());
        }

        Wine wine = (Wine) userAuth.setUser(newWine);
        return repository.save(wine);
    }

    @Override
    public Optional<Wine> edit(Long id, Wine editedWine) {
        Optional<Wine> wineOpt = findById(id);
        if (wineOpt.isEmpty()) {
            return Optional.empty();
        }

        Wine wine = wineOpt.get();
        validateEditPermission(wine);

        wine.setName(editedWine.getName());
        wine.setType(editedWine.getType());
        wine.setCountry(editedWine.getCountry());
        wine.setPrice(editedWine.getPrice());
        wine.setVolume(editedWine.getVolume());
        wine.setDescription(editedWine.getDescription());
        wine.setFoodPairings(editedWine.getFoodPairings());
        wine.setUrl(editedWine.getUrl());

        return Optional.of(repository.save(wine));
    }

    @Override
    public void delete(Long id) {
        findById(id).ifPresent(this::validateEditPermission);
        repository.deleteById(id);
    }

    // --- Other methods ---
    @Override
    public int count() {
        return (int) repository.count();
    }

    @Override
    public boolean isAllowedToEdit(Long id) {
        return findById(id)
                .map(userAuth::isUserAllowedToEdit)
                .orElse(false);
    }

    @Override
    public boolean isValid(String name) {
        return !repository.existsByName(name);
    }

    @Override
    public List<String> findCountries() {
        return repository.findAllCountries();
    }

    @Override
    public List<String> findDescriptions() {
        return repository.findAllDescriptions();
    }

    @Override
    public List<String> findFoodPairings() {
        return repository.findAllFoodPairings();
    }

    @Override
    public List<Wine> search(
            String name,
            String type,
            List<String> countries,
            List<Double> volumes,
            List<Double> priceRange) {

        if (name == null && type == null && countries == null && volumes == null && priceRange == null) {
            return Collections.emptyList();
        }

        WineSearchParams searchParams = WineSearchParams
                .builder()
                .name(name)
                .type(type)
                .countries(countries)
                .volumes(volumes)
                .priceRange(priceRange)
                .build();

        return repository.findAll(new WineSpecification(searchParams));
    }

    private void validateEditPermission(Wine wine) {
        if (!userAuth.isUserAllowedToEdit(wine)) {
            throw new ForbiddenException("wine");
        }
    }
}