package com.webflux.weather.bean;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean

public class Clouds{

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    private int all;
}