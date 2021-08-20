package com.webflux.weather.controller;

import com.webflux.weather.entity.City;
import com.webflux.weather.service.CityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.Assert.assertEquals;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class WeatherControllerTest {
    @InjectMocks
    WeatherController weatherController;
    @Mock
    CityService cityService;
    @Mock
    DynamoDbAsyncTable<City> cityDynamoDbAsyncTable;
    @Test
    public void testGetCity() {
        City city = new City();
        city.setName("UK");
        when(cityService.getByCity("London", "UK")).thenReturn(Mono.just(city));
        //when(cityDynamoDbAsyncTable.getItem(builder -> {})).thenReturn(CompletableFuture.completedFuture(city));
        Mono<City> cityMono =  weatherController.getWeather("London", "UK");
        StepVerifier.create(cityMono.log())
                .expectNext(city)
                .verifyComplete();
    }

}
