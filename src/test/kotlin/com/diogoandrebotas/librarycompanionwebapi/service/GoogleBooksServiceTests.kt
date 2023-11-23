package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.config.HttpClientConfig
import com.diogoandrebotas.librarycompanionwebapi.model.GoogleBooksImageLinks
import com.diogoandrebotas.librarycompanionwebapi.model.GoogleBooksItem
import com.diogoandrebotas.librarycompanionwebapi.model.GoogleBooksResponse
import com.diogoandrebotas.librarycompanionwebapi.model.GoogleBooksVolumeInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExtendWith(MockitoExtension::class)
class GoogleBooksServiceTests {
    @Test
    fun `getBookWithIsbn returns GoogleBooksResponse when the book is found`() {
        val expectedResponse = GoogleBooksResponse(
            totalItems = 1,
            items = listOf(
                GoogleBooksItem(
                    volumeInfo = GoogleBooksVolumeInfo(
                        title = "Dune",
                        authors = listOf("Frank Herbert"),
                        publishedDate = "2019-10",
                        pageCount = 658,
                        imageLinks = GoogleBooksImageLinks(
                            thumbnail = "https://books.google.com/books/content?id=3o2TDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                        )
                    )
                )
            )
        )
        val httpClient = HttpClient(
            MockEngine {
                respond(
                    content = """
                    {
                      "totalItems": 1,
                      "items": [
                        {
                          "volumeInfo": {
                            "title": "Dune",
                            "authors": [
                              "Frank Herbert"
                            ],
                            "publishedDate": "2019-10",
                            "pageCount": 658,
                            "imageLinks": {
                              "thumbnail": "https://books.google.com/books/content?id=3o2TDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                            }
                          }
                        }
                      ]
                    }
                """.trimIndent(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            })  {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
        val httpClientConfig = mock<HttpClientConfig> {
            on { httpClient() } doReturn httpClient
        }
        val googleBooksService = GoogleBooksService(httpClientConfig)

        val actualResponse = runBlocking {
            googleBooksService.getBookWithIsbn("9780593099322")
        }

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `getBookWithIsbn returns GoogleBooksResponse when the book is not found`() {
        val expectedResponse = GoogleBooksResponse(
            totalItems = 0,
            items = listOf()
        )
        val httpClient = HttpClient(
            MockEngine {
                respond(
                    content = """
                    {
                      "totalItems": 0
                    }
                """.trimIndent(),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            })  {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

        val httpClientConfig = mock<HttpClientConfig> {
            on { httpClient() } doReturn httpClient
        }

        val googleBooksService = GoogleBooksService(httpClientConfig)

        val actualResponse = runBlocking {
            googleBooksService.getBookWithIsbn("9780593099322")
        }

        assertEquals(expectedResponse, actualResponse)
    }
}