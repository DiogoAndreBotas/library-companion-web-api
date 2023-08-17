package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.model.GoogleBooksResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class GoogleBooksService(
    private val httpClient: HttpClient,
    private val s3Service: S3Service
) {
    fun getBookWithIsbn(isbn: String): GoogleBooksResponse {
        val url = "https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn"

        return runBlocking {
            httpClient.get(url).body()
        }
    }

    fun uploadCoverToS3(isbn: String, url: String) {
        runBlocking {
            val byteArray = httpClient.get(url).readBytes()
            s3Service.uploadImage(isbn, byteArray)
        }
    }
}