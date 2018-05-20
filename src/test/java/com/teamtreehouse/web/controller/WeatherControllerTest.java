package com.teamtreehouse.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WeatherControllerTest {

    private MockMvc mockMvc;
    private WeatherController controller;

    @Before
    public void setUp() throws Exception {
        controller = new WeatherController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void homeShouldRenderDetailView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name("weather/detail"));
    }

    @Test
    public void searchShouldRedirectWithAPathParameter() throws Exception {
        mockMvc.perform(get("/search").param("q", "60622"))
                .andExpect(redirectedUrl("/search/60622"));
    }
}
