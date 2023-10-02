package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebFlux
public class MoviesInfoControllerUnitTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private MoviesInfoService moviesInfoServiceMock;

    @Test
    void getAll() {
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")), new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")), new MovieInfo("abc", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        when(moviesInfoServiceMock.getAll()).thenReturn(Flux.fromIterable(movieInfos));
        webTestClient.get().uri("/v1/movieinfos").exchange().expectStatus().is2xxSuccessful().expectBodyList(MovieInfo.class).hasSize(3);
    }

    @Test
    void getById() {
        var movieInfo = new MovieInfo("1", "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        when(moviesInfoServiceMock.getById("1")).thenReturn(Mono.just(movieInfo));
        webTestClient.get().uri("/v1/movieinfos/{id}", "1").exchange().expectStatus().is2xxSuccessful().expectBody(MovieInfo.class).consumeWith(movieInfoEntityExchangeResult -> {
            var info = movieInfoEntityExchangeResult.getResponseBody();
            assertNotNull(info);
            assertEquals(movieInfo, info);
        });
    }

    @Test
    void add() {
        var info = new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        when(moviesInfoServiceMock.add(isA(MovieInfo.class))).thenReturn(Mono.just(new MovieInfo("mockId", "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"))));
        webTestClient.post().uri("/v1/movieinfos").bodyValue(info).exchange().expectStatus().is2xxSuccessful().expectBody(MovieInfo.class).consumeWith(movieInfoEntityExchangeResult -> {
            var saved = movieInfoEntityExchangeResult.getResponseBody();
            assertNotNull(saved);
            assertNotNull(saved.getMovieInfoId());
            assertEquals("mockId", saved.getMovieInfoId());
        });
    }

    @Test
    void add_valid() {
        var info = new MovieInfo(null, "", -2005, List.of(""), LocalDate.parse("2005-06-15"));
        when(moviesInfoServiceMock.add(isA(MovieInfo.class))).thenReturn(Mono.just(new MovieInfo("mockId", "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"))));
        webTestClient.post().uri("/v1/movieinfos").bodyValue(info).exchange().expectStatus().isBadRequest().expectBody(String.class).consumeWith(stringEntityExchangeResult -> {
            var responseBody = stringEntityExchangeResult.getResponseBody();
            assertEquals("movieInfo.cast must be present,movieInfo.name must be present,movieInfo.year must be positive value", responseBody);
        });
    }

    @Test
    void update() {
        var info = new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2023-06-15"));
        when(moviesInfoServiceMock.update(isA(MovieInfo.class), isA(String.class))).thenReturn(Mono.just(new MovieInfo("mockId", "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"))));
        webTestClient.put().uri("/v1/movieinfos/{id}", "abc").bodyValue(info).exchange().expectStatus().is2xxSuccessful().expectBody(MovieInfo.class).consumeWith(movieInfoEntityExchangeResult -> {
            var updated = movieInfoEntityExchangeResult.getResponseBody();
            assertNotNull(updated);
            assertNotNull(updated.getMovieInfoId());
            assertEquals("Batman Begins", updated.getName());
        });
    }

    @Test
    void delete() {
        when(moviesInfoServiceMock.delete(isA(String.class))).thenReturn(Mono.empty());
        webTestClient.delete().uri("/v1/movieinfos/{id}", "1").exchange().expectStatus().isNoContent();
    }
}
