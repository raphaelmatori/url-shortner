package com.urlshortener.repository

import com.urlshortener.model.ShortUrl
import org.springframework.data.mongodb.repository.MongoRepository

interface ShortUrlRepository : MongoRepository<ShortUrl, String>
