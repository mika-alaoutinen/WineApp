package com.mika.WineApp.it.reviews;

import com.mika.WineApp.models.user.User;
import com.mika.WineApp.security.model.UserPrincipal;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewsCrudReadIT extends ReviewTest {
    private static final String ENDPOINT = "/reviews";

    @Test
    void getAllReviews() throws Exception {
        mvc
                .perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getReviewById() throws Exception {
        mvc
                .perform(get(ENDPOINT + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Mika"))
                .andExpect(jsonPath("$.rating").value(3.0));
    }

    @Test
    void isReviewEditable() throws Exception {
        UserPrincipal user = new UserPrincipal(new User("test_user", "password"));

        mvc
                .perform(get(ENDPOINT + "/1/editable").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
