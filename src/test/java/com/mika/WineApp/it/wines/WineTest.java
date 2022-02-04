package com.mika.WineApp.it.wines;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestRead;
import com.mika.WineApp.repositories.UserRepository;
import com.mika.WineApp.repositories.WineRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTestRead
class WineTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WineRepository wineRepository;

    @Autowired
    MockMvc mvc;

    @BeforeAll
    void setupRepositories() {
        userRepository.saveAll(TestData.initTestUsers());
        wineRepository.saveAll(TestData.initWines());
    }
}
