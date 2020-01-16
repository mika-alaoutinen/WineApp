package com.mika.WineApp.configuration;

import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineParser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * This class is used to initiate a database for the first time. The bean uses a Parser class
 * to parse reviews from the texts folder. The parsed reviews are then saved into a database.
 */
@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(WineRepository wineRepository, ReviewRepository reviewRepository) {

        if (!wineRepository.findAll().isEmpty() && !reviewRepository.findAll().isEmpty()) {
            log.info("Repositories are already initiated, skipping database preloading!");
            return args -> {};
        }

        // Try to parse wines and reviews into database:
        Parser parser = new Parser();

        return args -> {
            log.info("Preloading database.");

            wineRepository.saveAll(parser.getWines());
            log.info("Wines loaded successfully!");

            reviewRepository.saveAll(parser.getReviews());
            log.info("Reviews loaded successfully!");
        };
    }
}