package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Favorite;
import com.teamtreehouse.service.FavoriteNotFoundException;
import com.teamtreehouse.service.FavoriteService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FavoriteControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private FavoriteController controller;

    @Mock
    private FavoriteService service;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void indexShouldIncludeFavoritesInModel() throws Exception {
        // Arrange the mock behaviour
        List<Favorite> favorites = Arrays.asList(
                new Favorite.FavoriteBuilder(1L).withAddress("Makati").withPlaceId("makati1").build(),
                new Favorite.FavoriteBuilder(2L).withAddress("Manila").withPlaceId("manila2").build()
        );
       when(service.findAll()).thenReturn(favorites);

        // Act: perform the MVC request and Assert the results
        mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("favorite/index"))
                .andExpect(model().attribute("favorites", favorites));
        verify(service).findAll();
    }

    @Test
    public void addShouldRedirectToNewFavorite() throws Exception {
        doAnswer(invocation -> {
            Favorite f = (Favorite) invocation.getArguments()[0];
            f.setId(1L);
            return null;
        }).when(service).save(any(Favorite.class));

        mockMvc.perform(
                post("/favorites")
                .param("formattedAddress", "chicago, il")
                .param("placeId", "windycity")
        ).andExpect(redirectedUrl("/favorites/1"));

        verify(service).save(any(Favorite.class));
    }

    @Test
    public void detailShouldErrorOnNotFound() throws Exception {
        when(service.findById(1L)).thenThrow(FavoriteNotFoundException.class);

        mockMvc.perform(get("/favorites/1"))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("ex", Matchers.instanceOf(FavoriteNotFoundException.class)));

        verify(service).findById(1L);
    }
}