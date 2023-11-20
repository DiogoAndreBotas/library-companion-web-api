package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.config.HttpClientConfig
import com.diogoandrebotas.librarycompanionwebapi.model.GoogleBooksResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class GoogleBooksService(
    private val httpClientConfig: HttpClientConfig
) {
    fun getBookWithIsbn(isbn: String): GoogleBooksResponse {
        return runBlocking {
            httpClientConfig.httpClient()
                .get("https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn")
                .body()
        }
    }
}