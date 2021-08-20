package com.webflux.weather.repository;

import com.webflux.weather.entity.City;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.concurrent.CompletableFuture;

@Repository
public class CityRepository {
    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<City> CityDynamoDbAsyncTable;

    public CityRepository(DynamoDbEnhancedAsyncClient asyncClient) {
        this.enhancedAsyncClient = asyncClient;
        this.CityDynamoDbAsyncTable = enhancedAsyncClient.table(City.class.getSimpleName(), TableSchema.fromBean(City.class));
    }

    public CompletableFuture<Void> save(City city) {
        return CityDynamoDbAsyncTable.putItem(city);
    }

    //READ
    public CompletableFuture<City> getCityByID(String name) {
        return CityDynamoDbAsyncTable.getItem(getKeyBuild(name));
    }

    public CompletableFuture<City> deleteCityByID(String name) {
        return CityDynamoDbAsyncTable.deleteItem(getKeyBuild(name));
    }

    public PagePublisher<City> getAllCity() {
        return CityDynamoDbAsyncTable.scan();
    }

    private Key getKeyBuild(String cityName) {
        return Key.builder().partitionValue(cityName).build();
    }
}
