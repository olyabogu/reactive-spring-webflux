package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {
    private FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        var namesFlux = service.namesFlux();
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {
        var namesFlux = service.namesFlux_map(3);
        StepVerifier.create(namesFlux)
                .expectNext("4-ALEX", "5-CHLOE")
//                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap() {
        var namesFlux = service.namesFlux_flatMap(3);
        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap_async() {
        var namesFlux = service.namesFlux_flatMap_async(3);
        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        var namesFlux = service.namesFlux_concatMap(3);
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap() {
        var namesMonoFlatMap = service.namesMono_flatMap(3);
        StepVerifier.create(namesMonoFlatMap)
                .expectNext(List.of("A", "L", "E", "X"))
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_immutability() {
        var namesFlux = service.namesFlux_immutability();
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .verifyComplete();
    }
}