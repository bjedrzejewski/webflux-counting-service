package com.e4developer.webfluxservice;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/numbers")
public class NumbersController {

    /**
     * Returns value immediately as Flux.range() completes automatically
     * @param number
     * @return
     */
    @GetMapping(path = "/count/{number}")
    public Flux<Integer> countToNumber(
            @PathVariable("number") int number) {
        return Flux.range(0, number);
    }

    /**
     * If accessed from normal browser it only returns when sink
     * method complete() is called
     * @param number
     * @return
     */
    @GetMapping(path = "/slow_count/{number}")
    public Flux<Integer> slowCountToNumber(
            @PathVariable("number") int number) {
        Flux<Integer> dynamicFlux = Flux.create(sink -> {
            SlowCounter.count(sink, number);
        });
        return dynamicFlux;
    }

    /**
     * Streams values as they are sent
     * Based on Server sent events
     * @param number
     * @return
     */
    @CrossOrigin
    @GetMapping(path = "/stream_count/{number}", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> streamCountToNumber(
            @PathVariable("number") int number) {
        Flux<Integer> dynamicFlux = Flux.create(sink -> {
            SlowCounter.count(sink, number);
        });
        return dynamicFlux;
    }
}