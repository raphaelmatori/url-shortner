package com.urlshortener.controller

import com.urlshortener.service.UrlService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

@RestController
class UrlController(private val service: UrlService) {

    data class ShortenRequest(val url: String)

    @PostMapping("/shorten")
    fun shorten(@RequestBody body: ShortenRequest) = service.createShortUrl(body.url)

    @GetMapping("/{id}")
    fun redirect(@PathVariable id: String): Any {
        val url = service.resolveShortUrl(id)
        return if (url != null) RedirectView(url) else ResponseEntity.status(HttpStatus.FOUND)
    }
}
