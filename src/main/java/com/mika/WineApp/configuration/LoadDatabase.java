package com.mika.WineApp.configuration;

import com.mika.WineApp.models.user.Role;
import com.mika.WineApp.models.user.User;
import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineParser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

/*
 * This class is used to initiate a database for the first time. The bean uses a Parser class
 * to parse reviews from the texts folder. The parsed reviews are then saved into a database.
 * An admin user is set as the owner for all parsed wines and reviews.
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
class LoadDatabase {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   WineRepository wineRepository,
                                   ReviewRepository reviewRepository) {

        if (wineRepository.count() != 0 && reviewRepository.count() != 0) {
            log.info("Repositories are already initiated, skipping database preloading!");
            return args -> {};
        }

        // Get admin to set as owner for parsed reviews and wines:
        User admin = userRepository.findByUsername(adminUsername)
                .orElse(initAdminUser(userRepository));

        // Try to parse wines and reviews into database:
        Parser parser = new Parser();

        return args -> {
            log.info("Preloading database.");
            initWines(parser, wineRepository, admin);
            initReviews(parser, reviewRepository, admin);
        };
    }

    private User initAdminUser(UserRepository repository) {
        User admin = new User(adminUsername, passwordEncoder.encode(adminPassword));
        admin.setRoles(Set.of(Role.ROLE_ADMIN));
        repository.save(admin);
        log.info("Admin user created successfully!");
        return admin;
    }

    private void initWines(Parser parser, WineRepository repository, User admin) {
        var wines = parser.getWines();
        wines.forEach(wine -> wine.setUser(admin));
        repository.saveAll(wines);
        log.info("Wines loaded successfully!");
    }

    private void initReviews(Parser parser, ReviewRepository repository, User admin) {
        var reviews = parser.getReviews();
        reviews.forEach(review -> review.setUser(admin));
        repository.saveAll(reviews);
        log.info("Reviews loaded successfully!");
    }
}