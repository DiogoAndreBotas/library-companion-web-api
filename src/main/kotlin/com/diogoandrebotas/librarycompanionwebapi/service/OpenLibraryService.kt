package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.model.OpenLibraryAuthorResponse
import com.diogoandrebotas.librarycompanionwebapi.model.OpenLibraryBookResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class OpenLibraryService(
    private val httpClient: HttpClient,
    private val s3Service: S3Service
) {

    fun getBookWithIsbn(isbn: String): OpenLibraryBookResponse {
        val url = "https://openlibrary.org/isbn/$isbn.json"

        return runBlocking {
            httpClient.get(url).body()
        }
    }

    fun getAuthorWithKey(key: String): OpenLibraryAuthorResponse {
        val url = "https://openlibrary.org/$key.json"

        return runBlocking {
            httpClient.get(url).body()
        }
    }

    fun getCoverWithId(id: Int, isbn: String) {
        val url = "https://covers.openlibrary.org/b/id/$id.jpg"
        val byteArray = runBlocking {
            httpClient.get(url).readBytes()
        }

        runBlocking {
            s3Service.uploadImage(isbn, byteArray)
        }
    }

}