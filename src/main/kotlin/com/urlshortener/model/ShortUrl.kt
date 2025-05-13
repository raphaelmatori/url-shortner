package com.urlshortener.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("short_urls")
data class ShortUrl(
    @Id
    val id: String, // base62 key
    val originalUrl: String,
    val createdAt: Instant = Instant.now(),
    @Indexed(expireAfter = "2592000") // 30 days
    val expiresAt: Instant = Instant.now().plusSeconds(2592000),
    val clicks: Long = 0
)
