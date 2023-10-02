package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesInfoService {
    private final MovieInfoRepository movieInfoRepository;

    public MoviesInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> add(MovieInfo moviesInfo) {
        return movieInfoRepository.save(moviesInfo);
    }

    public Flux<MovieInfo> getAll() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> update(MovieInfo updatedMovieInfo, String id) {
        return movieInfoRepository.findById(id).flatMap(movieInfo -> {
            movieInfo.setCast(updatedMovieInfo.getCast());
            movieInfo.setName(updatedMovieInfo.getName());
            movieInfo.setReleaseDate(updatedMovieInfo.getReleaseDate());
            movieInfo.setYear(updatedMovieInfo.getYear());
            return movieInfoRepository.save(movieInfo);
        });
    }

    public Mono<Void> delete(String id) {
        return movieInfoRepository.deleteById(id);
    }

    public Flux<MovieInfo> getByYear(Integer year) {
        return movieInfoRepository.findByYear(year);
    }

    public Flux<MovieInfo> getByName(String name) {
        return movieInfoRepository.findByName(name);
    }
}
