package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class FluxAndMonoGeneratorService {
    public static void main(String[] args) {
        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();
        service.namesFlux().subscribe(name -> {
            System.out.println("flux name is : " + name);
        });

        service.nameMono().subscribe(name -> {
            System.out.println("mono name is : " + name);
        });
    }

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Flux<String> namesFlux_map(int lenght) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase).filter(s -> s.length() > lenght)
                .map(s -> s.length() + "-" + s).log();
    }

    public Mono<List<String>> namesMono_flatMap(int lenght) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > lenght)
//                .map(s -> s.length() + "-" + s)
                .flatMap(this::splitStringMono).log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public Flux<String> namesFlux_flatMap(int lenght) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase).filter(s -> s.length() > lenght)
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> namesFlux_concatMap(int lenght) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase).filter(s -> s.length() > lenght)
                .concatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> namesFlux_flatMap_async(int lenght) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase).filter(s -> s.length() > lenght)
                .flatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitString_withDelay(String name) {
        var charArray = name.split("");
//        var delay = new Random().nextInt(1000);
        var delay = 1000;
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }

    public Flux<String> namesFlux_immutability() {
        var namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        namesFlux.map(String::toUpperCase);
        return namesFlux;

    }

    public Mono<String> nameMono() {
        return Mono.just("alex").log();
    }


}
