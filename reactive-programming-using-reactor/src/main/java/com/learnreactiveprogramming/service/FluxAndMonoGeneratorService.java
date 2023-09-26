package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

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

    public Flux<String> namesMono_flatMapMany(int lenght) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > lenght)
                .flatMapMany(this::splitString).log();
    }

    public Flux<String> namesFlux_transform(int lenght) {
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > lenght);
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(this::splitString).log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int lenght) {
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > lenght);
        var defaultFlux = Flux.just("default").transform(filterMap);
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux);
    }

    public Flux<String> namesFlux_transform_default(int lenght) {
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > lenght);
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .defaultIfEmpty("default");
    }

    public Flux<String> explore_concat() {
        var abc = Flux.just("A", "B", "C");
        var def = Flux.just("D", "E", "F");
        return Flux.concat(abc, def);
    }

    public Flux<String> concatWith() {
        var abc = Flux.just("A", "B", "C");
        var def = Flux.just("D", "E", "F");
        return abc.concatWith(def);
    }

    public Flux<String> concatWithMono() {
        var a = Mono.just("A");
        var b = Mono.just("B");

        return a.concatWith(b).log();
    }

    public Flux<String> explore_merge() {
        var abc = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var def = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));
        return Flux.merge(abc, def).log();
    }

    public Flux<String> explore_mergeWith() {
        var abc = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var def = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));
        return abc.mergeWith(def);
    }

    public Flux<String> explore_mergeWithMono() {
        var a = Mono.just("A");
        var b = Mono.just("B");
        return a.mergeWith(b);
    }

    public Flux<String> explore_mergeSequential() {
        var abc = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var def = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(abc, def);
    }

    public Flux<String> explore_zip() {
        var abc = Flux.just("A", "B", "C");
        var def = Flux.just("D", "E", "F");
        return Flux.zip(abc, def, (first, second) -> first + second);
    }

    public Flux<String> explore_zip_t4() {
        var abc = Flux.just("A", "B", "C");
        var def = Flux.just("D", "E", "F");
        var _123 = Flux.just("1", "2", "3");
        var _456 = Flux.just("4", "5", "6");
        return Flux.zip(abc, def, _123, _456).map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4());
    }

    public Flux<String> explore_zipWith() {
        var abc = Flux.just("A", "B", "C");
        var def = Flux.just("D", "E", "F");
        return abc.zipWith(def, (first, second) -> first + second);
    }

    public Mono<String> explore_zipWithMono() {
        var a = Mono.just("A");
        var b = Mono.just("B");
        return a.zipWith(b).map(t2 -> t2.getT1() + t2.getT2());
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
