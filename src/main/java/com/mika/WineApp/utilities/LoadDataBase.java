package com.mika.WineApp.utilities;

import com.mika.WineApp.repositories.ReviewRepository;
import com.mika.WineApp.repositories.WineRepository;
import com.mika.WineParser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDataBase {

    @Bean
    CommandLineRunner initDatabase(WineRepository wineRepository, ReviewRepository reviewRepository) {
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
