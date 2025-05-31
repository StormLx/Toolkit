package com.example.demo.controller;

import com.example.demo.config.SecurityConfig; // Make sure SecurityConfig is imported
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicController.class)
@Import(SecurityConfig.class) // Import SecurityConfig to apply security rules
public class PublicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPublicMessage_shouldReturnPublicMessage() throws Exception {
        mockMvc.perform(get("/api/public/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from Public API!"));
    }
}
