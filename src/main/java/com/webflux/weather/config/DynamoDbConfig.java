package com.webflux.weather.config;

import com.webflux.weather.entity.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {
    private final String dynamoDbEndPointUrl;
    private final String dynamoDbaccessKey;
    private final String dynamoDbprivateKey;
    public DynamoDbConfig(@Value("${aws.dynamodb.endpoint}") String dynamoDbEndPointUrl,
                          @Value("${aws.dynamodb.access}") String dynamoDbaccessKey,
                          @Value("${aws.dynamodb.private}") String dynamoDbprivateKey) {
        this.dynamoDbEndPointUrl = dynamoDbEndPointUrl;
        this.dynamoDbaccessKey = dynamoDbaccessKey;
        this.dynamoDbprivateKey = dynamoDbprivateKey;
    }
    @Bean
    AwsBasicCredentials awsBasicCredentials(){
        return AwsBasicCredentials.create(dynamoDbaccessKey, dynamoDbprivateKey);
    }

    @Bean
    public DynamoDbAsyncClient getDynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials()))
                                  //.endpointOverride(URI.create("http://dynamodb"+ Region.US_EAST_2 + "amazonaws.com"))
                //Aws will connect to the DB created in this region.
                .region(Region.US_EAST_2).build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient(DynamoDbAsyncClient dynamoDbAsyncClient) {
        return DynamoDbEnhancedAsyncClient.builder()
                                          .dynamoDbClient(dynamoDbAsyncClient)
                                          .build();
    }



    @Bean
    public DynamoDbAsyncTable<City> getDynamoDbAsyncCustomer(DynamoDbEnhancedAsyncClient asyncClient) {
        return asyncClient.table(City.class.getSimpleName(), TableSchema.fromBean(City.class));
    }

}