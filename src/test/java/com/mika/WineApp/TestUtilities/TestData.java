package com.mika.WineApp.TestUtilities;

import com.mika.WineApp.models.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface TestData {

    static List<Review> initReviews() {
        User user = initTestUsers().get(0);

        var date1 = LocalDate.of(2019, 11, 14);
        var date2 = LocalDate.of(2019, 11, 15);

        Review r1 = Review
                .builder()
                .author("Mika")
                .date(date1)
                .reviewText("Mikan uusi arvostelu")
                .rating(3.0)
                .user(user)
                .build();

        Review r2 = Review
                .builder()
                .author("Salla")
                .date(date2)
                .reviewText("Sallan uusi arvostelu")
                .rating(4.5)
                .user(user)
                .build();

        return List.of(r1, r2);
    }

    static List<Wine> initWines() {
        var description1 = List.of("puolikuiva", "sitruunainen", "yrttinen");
        var description2 = List.of("kuiva", "sitruunainen", "pirskahteleva");
        var description3 = List.of("tanniininen", "mokkainen", "täyteläinen", "tamminen");

        var foodPairings1 = List.of("kala", "kasvisruoka", "seurustelujuoma");
        var foodPairings2 = List.of("kana", "kasvisruoka", "juustot");
        var foodPairings3 = List.of("nauta", "pataruuat", "pasta");

        Wine white1 = Wine
                .builder()
                .name("Valkoviini 1")
                .type(WineType.WHITE)
                .country("Espanja")
                .price(8.75)
                .volume(0.75)
                .description(description1)
                .foodPairings(foodPairings1)
                .url("invalid")
                .reviews(Collections.emptyList())
                .build();

        Wine white2 = Wine
                .builder()
                .name("Valkoviini 2")
                .type(WineType.WHITE)
                .country("Espanja")
                .price(8.75)
                .volume(0.75)
                .description(description2)
                .foodPairings(foodPairings2)
                .url("invalid")
                .reviews(Collections.emptyList())
                .build();

        Wine red1 = Wine
                .builder()
                .name("Punaviini 1")
                .type(WineType.RED)
                .country("Ranska")
                .price(29.95)
                .volume(3.0)
                .description(description3)
                .foodPairings(foodPairings3)
                .url("invalid")
                .reviews(Collections.emptyList())
                .build();

        Wine red2 = Wine
                .builder()
                .name("Punaviini 2")
                .type(WineType.RED)
                .country("Italia")
                .price(30.95)
                .volume(3.0)
                .description(description3)
                .foodPairings(foodPairings3)
                .url("invalid")
                .reviews(Collections.emptyList())
                .build();

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