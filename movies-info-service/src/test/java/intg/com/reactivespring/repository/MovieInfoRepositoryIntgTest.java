package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
public class MovieInfoRepositoryIntgTest {
    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieInfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    public void findAll() {
        var movieInfoFlux = movieInfoRepository.findAll().log();
        StepVerifier.create(movieInfoFlux).expectNextCount(3).verifyComplete();
    }

    @Test
    public void findByYear() {
        var movieInfoFlux = movieInfoRepository.findByYear(2005).log();
        StepVerifier.create(movieInfoFlux).assertNext(movieInfo -> assertEquals("Batman Begins",
                movieInfo.getName())).verifyComplete();
    }

    @Test
    public void findByName() {
        var movieInfoFlux = movieInfoRepository.findByName("Batman Begins").log();
        StepVerifier.create(movieInfoFlux).assertNext(movieInfo -> assertEquals("Batman Begins",
                movieInfo.getName())).verifyComplete();
    }

    @Test
    public void findById() {
        var movieInfoMono = movieInfoRepository.findById("abc").log();
        StepVerifier.create(movieInfoMono).assertNext(movieInfo -> assertEquals("Dark Knight Rises", movieInfo.getName())).verifyComplete();
    }

    @Test
    public void save() {
        var movieInfo = new MovieInfo(null, "Batman Begins __",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        var movieInfoMono = movieInfoRepository.save(movieInfo).log();
        StepVerifier.create(movieInfoMono).assertNext(info -> {
            assertNotNull(info.getMovieInfoId());
            assertEquals("Batman Begins __", info.getName());
        }).verifyComplete();
    }

    @Test
    public void update() {
        var movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2023);
        var movieInfoMono = movieInfoRepository.save(movieInfo).log();
        StepVerifier.create(movieInfoMono).assertNext(info -> {
            assertEquals(2023, info.getYear());
        }).verifyComplete();
    }

    @Test
    public void deleteById() {
        movieInfoRepository.deleteById("abc").block();
        var movieInfoFlux = movieInfoRepository.findAll().log();
        StepVerifier.create(movieInfoFlux).expectNextCount(2).verifyComplete();
    }
}
