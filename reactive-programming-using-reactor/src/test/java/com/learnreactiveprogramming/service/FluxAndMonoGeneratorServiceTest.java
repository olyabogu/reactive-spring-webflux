package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {
    private final FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

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

    @Test
    void namesMono_flatMapMany() {
        var namesMonoFlatMap = service.namesMono_flatMapMany(3);
        StepVerifier.create(namesMonoFlatMap)
                .expectNext("A", "L", "E", "X")
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        var namesFlux = service.namesFlux_transform(3);
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_default() {
        var namesFlux = service.namesFlux_transform_default(6);
        StepVerifier.create(namesFlux)
                .expectNext("default")
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchIfEmpty() {
        var namesFlux = service.namesFlux_transform_switchIfEmpty(6);
        StepVerifier.create(namesFlux)
                .expectNext("DEFAULT")
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        var concatFlux = service.explore_concat();
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
//                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void concatWith() {
        var concatFlux = service.concatWith();
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
//                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void concatWithMono() {
        var concatFlux = service.concatWithMono();
        StepVerifier.create(concatFlux)
                .expectNext("A", "B")
//                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        var flux = service.explore_merge();
        StepVerifier.create(flux)
                .expectNext("A", "D", "B", "E", "C", "F")
//                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_mergeWith() {
        var flux = service.explore_mergeWith();
        StepVerifier.create(flux)
                .expectNext("A", "D", "B", "E", "C", "F")
//                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_mergeWithMono() {
        var flux = service.explore_mergeWithMono();
        StepVerifier.create(flux)
                .expectNext("A", "B")
//                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        var flux = service.explore_mergeSequential();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C", "D", "E", "F")
//                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        var flux = service.explore_zip();
        StepVerifier.create(flux)
                .expectNext("AD", "BE", "CF")
//                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_zipWith() {
        var flux = service.explore_zipWith();
        StepVerifier.create(flux)
                .expectNext("AD", "BE", "CF")
//                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_zipWithMono() {
        var stringMono = service.explore_zipWithMono();
        StepVerifier.create(stringMono)
                .expectNext("AB")
//                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void explore_zip_t4() {
        var flux = service.explore_zip_t4();
        StepVerifier.create(flux)
                .expectNext("AD14", "BE25", "CF36")
//                .expectNextCount(3)
                .verifyComplete();
    }
}