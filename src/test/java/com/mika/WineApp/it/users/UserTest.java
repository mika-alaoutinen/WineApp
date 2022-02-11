package com.mika.WineApp.it.users;

import com.mika.WineApp.TestUtilities.TestData;
import com.mika.WineApp.it.IntegrationTestRead;
import com.mika.WineApp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTestRead
class UserTest {

    @Autowired
    UserRepository repository;

    @Autowired
    MockMvc mvc;

    @BeforeAll
    void setupRepository() {
        repository.saveAll(TestData.initTestUsers());
    }
}
