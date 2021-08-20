package com.webflux.weather.controller;

import com.webflux.weather.entity.City;
import com.webflux.weather.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class WeatherController {

    @Autowired
    private CityService cityService;

    @GetMapping("/show/{city}/{country}")
    public Mono<City> getWeather(@PathVariable String city, @PathVariable String country) {
        return cityService.getByCity(city, country);
    }

    @GetMapping("/showAll")
    public Flux<City> getAllCity() {
        return cityService.getAllCity();
    }


    @PostMapping("/add")
    public Mono<String> addCity(@RequestBody String city) {
        return Mono.just("Working");
    }


    @GetMapping("/delete/{city}")
    public Mono<String> deleteCity(@PathVariable String city) {
        return cityService.deleteCity(city);
    }


}
