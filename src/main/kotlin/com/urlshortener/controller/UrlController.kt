package com.urlshortener.controller

import com.urlshortener.model.ShortUrl
import org.slf4j.Logger
import com.urlshortener.service.UrlService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

@RestController
class UrlController(private val service: UrlService) {

    private val logger: Logger = LoggerFactory.getLogger(UrlController::class.java)

    data class ShortenRequest(val url: String)

    @PostMapping("/shorten")
    fun shorten(@RequestBody body: ShortenRequest): ResponseEntity<ShortUrl> {
        val start = System.currentTimeMillis()

        val shortUrl = service.createShortUrl(body.url)

        val duration = System.currentTimeMillis() - start
        logger.info("Shortened URL in ${duration}ms")

        return ResponseEntity.ok(shortUrl)
    }

    @GetMapping("/{id}")
    fun redirect(@PathVariable id: String): Any {
        val url = service.resolveShortUrl(id)
        return if (url != null) RedirectView(url) else ResponseEntity.status(HttpStatus.FOUND)
    }
}
