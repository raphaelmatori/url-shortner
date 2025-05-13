package com.urlshortener.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class MongoConfig {

    @Bean
    fun mongoClient(): MongoClient {
        val settings = MongoClientSettings.builder()
            .applyToConnectionPoolSettings { builder ->
                builder
                    .maxSize(100)
                    .minSize(10)
                    .maxWaitTime(1000, TimeUnit.MILLISECONDS)
            }
            .applyConnectionString(ConnectionString("mongodb://mongo:27017/urlshortener"))
            .build()

        return MongoClients.create(settings)
    }
}
