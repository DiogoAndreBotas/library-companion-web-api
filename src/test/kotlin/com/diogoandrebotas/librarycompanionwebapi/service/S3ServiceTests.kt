package com.diogoandrebotas.librarycompanionwebapi.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import aws.smithy.kotlin.runtime.content.ByteStream
import com.diogoandrebotas.librarycompanionwebapi.config.S3Config
import com.diogoandrebotas.librarycompanionwebapi.exception.S3FileNotFoundException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*

@ExtendWith(MockitoExtension::class)
class S3ServiceTests {

    @Test
    fun uploadImage_ShouldPutImageInS3Bucket() {
        // Given
        val s3Client = mock<S3Client> {
            onBlocking { putObject(any()) } doReturn PutObjectResponse {}
        }
        val s3Config = mock<S3Config> { on { client() } doReturn s3Client }
        val s3Service = S3Service(s3Config.client())

        // When
        runBlocking {
            s3Service.uploadImage("9780593099322", ByteArray(1000))
        }

        // Then
        verifyBlocking(s3Client) {
            putObject(any())
        }
    }

    @Test
    fun uploadImage_ShouldThrowAnException_WhenAnErrorOccursInS3() {
        // Given
        val s3Client = mock<S3Client> {
            onBlocking { putObject(any()) } doThrow S3Exception("The specified bucket does not exist")
        }
        val s3Config = mock<S3Config> { on { client() } doReturn s3Client }
        val s3Service = S3Service(s3Config.client())

        // When + Then
        assertThrows<S3Exception> {
            runBlocking {
                s3Service.uploadImage("9780593099322", ByteArray(1000))
            }
        }
    }


    @Test
    fun getImage_ShouldGetTheImageFromS3Bucket() {
        // Given
        val expectedByteArray = ByteArray(1000)
        val s3Client = mock<S3Client> {
            onBlocking {
                getObject<GetObjectResponse>(any(), any())
            } doReturn GetObjectResponse { body = ByteStream.fromBytes(expectedByteArray) }
        }
        val s3Config = mock<S3Config> { on { client() } doReturn s3Client }
        val s3Service = S3Service(s3Config.client())

        // When
        val actualByteArray = runBlocking {
            s3Service.getImage("9780593099322")
        }

        // Then
        assertEquals(expectedByteArray, actualByteArray)
    }

    @Test
    fun getImage_ShouldThrowAnException_WhenAnErrorOccursInS3() {
        // Given
        val s3Client = mock<S3Client> {
            onBlocking {
                getObject<GetObjectResponse>(any(), any())
            } doThrow S3Exception("The specified bucket does not exist")
        }
        val s3Config = mock<S3Config> { on { client() } doReturn s3Client }
        val s3Service = S3Service(s3Config.client())

        // When + Then
        assertThrows<S3Exception> {
            runBlocking {
                s3Service.getImage("9780593099322")
            }
        }
    }

    @Test
    fun getImage_ShouldThrowAnException_WhenTheImageIsNotFound() {
        // Given
        val s3Client = mock<S3Client> {
            onBlocking {
                getObject<GetObjectResponse>(any(), any())
            } doThrow NoSuchKey.invoke {}
        }
        val s3Config = mock<S3Config> { on { client() } doReturn s3Client }
        val s3Service = S3Service(s3Config.client())

        // When + Then
        assertThrows<S3FileNotFoundException> {
            runBlocking {
                s3Service.getImage("9780593099322")
            }
        }
    }

}