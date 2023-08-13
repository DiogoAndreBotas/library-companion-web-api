package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.config.HttpClientConfig
import com.diogoandrebotas.librarycompanionwebapi.model.OpenLibraryAuthorResponse
import com.diogoandrebotas.librarycompanionwebapi.model.OpenLibraryBookAuthorResponse
import com.diogoandrebotas.librarycompanionwebapi.model.OpenLibraryBookResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import org.springframework.stereotype.Service

@Service
class OpenLibraryService(
    private val httpClientConfig: HttpClientConfig,
    private val s3Service: S3Service
) {

    fun getBookWithIsbn(isbn: String): OpenLibraryBookResponse {
        val url = "https://openlibrary.org/isbn/$isbn.json"

        return runBlocking {
            httpClientConfig.httpClient().get(url).body()
        }
    }

    suspend fun getAuthorsAndUploadCoverToS3(bookResponse: OpenLibraryBookResponse, isbn: String): List<String> {
        return runBlocking {
            launch {
                uploadCoverToS3(bookResponse.covers.first(), isbn)
            }

            getAuthors(bookResponse.authors)
        }
    }

    private suspend fun getAuthors(authorIds: List<OpenLibraryBookAuthorResponse>): List<String> {
        return authorIds.map {
            val url = "https://openlibrary.org/${it.key}.json"
            httpClientConfig.httpClient().get(url).body<OpenLibraryAuthorResponse>().name
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun uploadCoverToS3(id: Int, isbn: String) {
        GlobalScope.launch {
            val byteArray = httpClientConfig.httpClient().get("https://covers.openlibrary.org/b/id/$id.jpg").readBytes()
            s3Service.uploadImage(isbn, byteArray)
        }
    }

}