package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1")
public class MoviesInfoController {
    private final MoviesInfoService moviesInfoService;

    public MoviesInfoController(MoviesInfoService moviesInfoService) {
        this.moviesInfoService = moviesInfoService;
    }

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAll(@RequestParam(value = "year", required = false) Integer year, @RequestParam(value =
            "name",
            required = false) String name) {
        if (name != null) {
            log.info("name is {}", name);
            return moviesInfoService.getByName(name);
        }
        if (year != null) {
            log.info("year is {}", year);
            return moviesInfoService.getByYear(year);
        }
        return moviesInfoService.getAll();
    }

    @GetMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> getById(@PathVariable String id) {
        return moviesInfoService.getById(id).map(movieInfo -> ResponseEntity.ok().body(movieInfo)).switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
    }

    @PutMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> update(@RequestBody MovieInfo movieInfo, @PathVariable String id) {
        return moviesInfoService.update(movieInfo, id).map(info -> ResponseEntity.ok().body(info)).switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return moviesInfoService.delete(id).log();
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> add(@RequestBody @Valid MovieInfo movieInfo) {
        return moviesInfoService.add(movieInfo);
    }
}
