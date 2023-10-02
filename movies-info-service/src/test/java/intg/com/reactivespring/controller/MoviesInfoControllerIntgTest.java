package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class MoviesInfoControllerIntgTest {
    @Autowired
    private MovieInfoRepository movieInfoRepository;
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")), new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")), new MovieInfo("abc", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
        movieInfoRepository.saveAll(movieInfos).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void add() {
        var info = new MovieInfo(null, "Batman Begins__", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        webTestClient.post().uri("/v1/movieinfos").bodyValue(info).exchange().expectStatus().is2xxSuccessful().expectBody(MovieInfo.class).consumeWith(movieInfoEntityExchangeResult -> {
            var saved = movieInfoEntityExchangeResult.getResponseBody();
            assertNotNull(saved);
            assertNotNull(saved.getMovieInfoId());
        });
    }

    @Test
    void update() {
        var info = new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2023-06-15"));
        webTestClient.put().uri("/v1/movieinfos/{id}", "abc").bodyValue(info).exchange().expectStatus().is2xxSuccessful().expectBody(MovieInfo.class).consumeWith(movieInfoEntityExchangeResult -> {
            var updated = movieInfoEntityExchangeResult.getResponseBody();
            assertNotNull(updated);
            assertNotNull(updated.getMovieInfoId());
            assertEquals("Batman Begins", updated.getName());
        });
    }

    @Test
    void update_notfound() {
        var info = new MovieInfo("edf", "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2023-06-15"));
        webTestClient.put().uri("/v1/movieinfos/{id}", "edf").bodyValue(info).exchange().expectStatus().isNotFound();
    }

    @Test
    void getAll() {
        webTestClient.get().uri("/v1/movieinfos").exchange().expectStatus().is2xxSuccessful().expectBodyList(MovieInfo.class).hasSize(3);
    }

    @Test
    void getById() {
        webTestClient.get().uri("/v1/movieinfos/{id}", "abc").exchange().expectStatus().is2xxSuccessful().expectBody(MovieInfo.class).consumeWith(movieInfoEntityExchangeResult -> {
            var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
            assertNotNull(movieInfo);
        });
    }

    @Test
    void getByYear() {
        var uri = UriComponentsBuilder.fromUriString("/v1/movieinfos").queryParam("year", 2005).buildAndExpand().toUri();
        webTestClient.get().uri(uri).exchange().expectStatus().is2xxSuccessful().expectBodyList(MovieInfo.class).hasSize(1);
    }

    @Test
    void getByName() {
        var uri = UriComponentsBuilder.fromUriString("/v1/movieinfos").queryParam("name", "Batman Begins").buildAndExpand().toUri();
        webTestClient.get().uri(uri).exchange().expectStatus().is2xxSuccessful().expectBodyList(MovieInfo.class).hasSize(1);
    }

    @Test
    void getById_notFound() {
        webTestClient.get().uri("/v1/movieinfos/{id}", "gfr").exchange().expectStatus().isNotFound();
    }

    @Test
    void getById2() {
        webTestClient.get().uri("/v1/movieinfos/{id}", "abc").exchange().expectStatus().is2xxSuccessful().expectBody().jsonPath("$.name").isEqualTo("Dark Knight Rises");
    }

    @Test
    void delete() {
        webTestClient.delete().uri("/v1/movieinfos/{id}", "abc").exchange().expectStatus().isNoContent();
    }

}
