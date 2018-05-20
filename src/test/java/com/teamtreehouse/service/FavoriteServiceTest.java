package com.teamtreehouse.service;

import com.teamtreehouse.dao.FavoriteDao;
import com.teamtreehouse.domain.Favorite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FavoriteServiceTest {
    @Mock
    private FavoriteDao dao;

    @InjectMocks
    private FavoriteService service = new FavoriteServiceImpl();

    @Test
    public void findAllShouldReturnTwo() throws Exception {
        List<Favorite> favorites = Arrays.asList(
                new Favorite(),
                new Favorite()
        );

        when(dao.findAll()).thenReturn(favorites);

        assertEquals("findAll should return 2 favorites", 2, service.findAll().size());
        verify(dao).findAll();
    }

    @Test
    public void findByIdShouldReturnOne() throws Exception {
        when(dao.findOne(1L)).thenReturn(new Favorite());
        assertThat(service.findById(1L), instanceOf(Favorite.class));
        verify(dao).findOne(1L);
    }

    @Test(expected = FavoriteNotFoundException.class)
    public void findByIdShouldThrowFavoriteNotFoundException() throws Exception {
        when(dao.findOne(1L)).thenReturn(null);

        service.findById(1L);
        verify(dao).findOne(1L);
    }
}