package com.webflux.weather.service;

import com.webflux.weather.bean.WeatherData;
import com.webflux.weather.entity.City;
import com.webflux.weather.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;
    private WebClient webClient;
    private String WEATHER_URL = "/data/2.5/weather";

    public CityService() {
        this.webClient = WebClient.create("http://api.openweathermap.org");
    }
    public CityService( CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    public Flux<City> getAllCity() {

        return  Flux.from(this.cityRepository.getAllCity().items())
        .onErrorReturn(new City());
    }
    public Mono<City> getByCity(String cityName, String countryName) {
        return  Mono.fromFuture(this.cityRepository.getCityByID(cityName)
                .thenApply((city) -> {

                ZonedDateTime now = ZonedDateTime.now();
                ZonedDateTime oneDaysAgo = now.plusDays(-1);
                if(city == null || city.getCreatedOn().isBefore(oneDaysAgo.toLocalDate())) {
                    if (city == null) {
                        city = new City();

                    }
                    city.setName(cityName);
                    city.setData(null);
                    city.setCreatedOn(LocalDate.now());
            }
            return city;
        })).flatMap(city -> city.getData() == null ? saveWeather(cityName, countryName, city) : Mono.just(city)).doOnNext(data -> {});


    }


    public Mono<City> saveWeather(String cityName, String countryName, City city) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/data/2.5/weather") //additional path
                        .queryParam("APPID", "b03ca3b30445ab9187e6c4413341922d")
                        .queryParam("q", cityName, countryName).build())
                .retrieve()
                .bodyToMono(WeatherData.class)
                .map(weatherData -> {
                    city.setData(weatherData);
                    return city;
                })
                .doOnNext(s-> {
                    this.cityRepository.save(city)
                            .handle((__, ex) ->{
                                return null;
                            });

                });
    }

    public Mono<String> deleteCity(String name) {
        return  Mono.fromFuture(this.cityRepository.deleteCityByID(name).handle((__, ex) ->{
            if(__ == null)
               return "Failed";
            return "Success";

        }));
    }
}
