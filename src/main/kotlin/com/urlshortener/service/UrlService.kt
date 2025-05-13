package com.urlshortener.service

import com.urlshortener.model.ShortUrl
import com.urlshortener.repository.ShortUrlRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UrlService(
    private val repo: ShortUrlRepository,
    private val redis: StringRedisTemplate
) {
    fun createShortUrl(original: String): ShortUrl {
        while (true) {
            val id = generateRandomId()
            if (!repo.existsById(id)) {
                val shortUrl = ShortUrl(id = id, originalUrl = original)
                repo.save(shortUrl)
                redis.opsForValue().set(id, original, 30, TimeUnit.DAYS)
                return shortUrl
            }
        }
    }

    fun resolveShortUrl(id: String): String? {
        return redis.opsForValue().get(id) ?: repo.findById(id).orElse(null)?.originalUrl
    }

    private fun generateRandomId(): String {
        val base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6).map { base62.random() }.joinToString("")
    }
}
