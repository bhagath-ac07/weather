package com.webflux.weather.repository;

import com.webflux.weather.entity.City;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Repository
public class RedisCityRepository {
    private final ReactiveRedisOperations reactiveRedisOperations;

    public RedisCityRepository(ReactiveRedisOperations reactiveRedisOperations) {
        this.reactiveRedisOperations = reactiveRedisOperations;
    }

    public Mono<City> save(City city) {
        reactiveRedisOperations.expire(city.getName(), Duration.of(24*60*60, ChronoUnit.SECONDS));
        return reactiveRedisOperations.opsForValue()
                .set(city.getName(), city.getData())
                .map(__ -> city);
    }

    public Flux<City> getAll(City city) {
        return reactiveRedisOperations.keys("*")
                .flatMap(reactiveRedisOperations.opsForValue()::get);
    }
}
