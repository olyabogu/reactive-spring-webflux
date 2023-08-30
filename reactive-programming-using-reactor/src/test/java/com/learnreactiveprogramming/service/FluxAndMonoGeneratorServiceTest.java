package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

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
    void namesFlux_immutability() {
        var namesFlux = service.namesFlux_immutability();
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .verifyComplete();
    }
}