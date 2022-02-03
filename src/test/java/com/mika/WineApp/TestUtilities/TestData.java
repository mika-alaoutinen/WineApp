package com.mika.WineApp.TestUtilities;

import com.mika.WineApp.models.review.Review;
import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.models.wine.Wine;
import com.mika.WineApp.models.wine.WineType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TestData {

    static List<Review> initReviews() {
        var wines = initWines();
        User user = initTestUsers().get(0);

        Wine wine1 = wines.get(0);
        Wine wine2 = wines.get(1);

        var date1 = LocalDate.of(2019, 11, 14);
        var date2 = LocalDate.of(2019, 11, 15);

        Review r1 = new Review("Mika", date1, "Mikan uusi arvostelu", 3.0, wine1);
        r1.setUser(user);

        Review r2 = new Review("Salla", date2, "Sallan uusi arvostelu", 4.5, wine2);
        r2.setUser(user);

        return List.of(r1, r2);
    }

    static List<Wine> initWines() {
        var description1 = List.of("puolikuiva", "sitruunainen", "yrttinen");
        var description2 = List.of("kuiva", "sitruunainen", "pirskahteleva");
        var description3 = List.of("tanniininen", "mokkainen", "täyteläinen", "tamminen");

        var foodPairings1 = List.of("kala", "kasvisruoka", "seurustelujuoma");
        var foodPairings2 = List.of("kana", "kasvisruoka", "juustot");
        var foodPairings3 = List.of("nauta", "pataruuat", "pasta");

        Wine white1 = new Wine("Valkoviini 1", WineType.WHITE, "Espanja", 8.75, 0.75, description1, foodPairings1, "invalid");
        Wine white2 = new Wine("Valkoviini 2", WineType.WHITE, "Espanja", 8.75, 0.75, description2, foodPairings2, "invalid");
        Wine red1 = new Wine("Punaviini 1", WineType.RED, "Ranska", 29.95, 3.0, description3, foodPairings3, "invalid");
        Wine red2 = new Wine("Punaviini 2", WineType.RED, "Italia", 30.95, 3.0, description3, foodPairings3, "invalid");

        User user = initTestUsers().get(0);
        white1.setUser(user);
        white2.setUser(user);
        red1.setUser(user);
        red2.setUser(user);

        return List.of(white1, white2, red1, red2);
    }

    static List<User> initTestUsers() {
        User user = new User("test_user", "test_user_password");
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setId(1L);

        User admin = new User("test_admin", "test_admin_password");
        admin.setRoles(Set.of(Role.ROLE_ADMIN));
        admin.setId(2L);

        return List.of(user, admin);
    }
}