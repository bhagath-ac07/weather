package com.webflux.weather.entity;

import com.webflux.weather.bean.WeatherData;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.LocalDateAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDate;


@NoArgsConstructor
@DynamoDbBean
public class City {
    private String name;
    private WeatherData weatherData;

    private LocalDate createdOn;

    //@DynamoDbConvertedBy(LocalDateAttributeConverter.class)
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    @DynamoDbPartitionKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WeatherData getData() {
        return weatherData;
    }

    public void setData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

}
