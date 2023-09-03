package com.diogoandrebotas.librarycompanionwebapi.service

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.NoSuchKey
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import com.diogoandrebotas.librarycompanionwebapi.exception.S3FileNotFoundException
import org.springframework.stereotype.Service

@Service
class S3Service(
    private val s3Client: S3Client
) {
    companion object {
        const val BUCKET_NAME = "library-companion-api-images"
    }

    suspend fun uploadImage(isbn: String, byteArray: ByteArray) {
        val request = PutObjectRequest {
            bucket = BUCKET_NAME
            key = "${isbn}.jpg"
            metadata = mutableMapOf()
            body = ByteStream.fromBytes(byteArray)
        }

        s3Client.putObject(request)
    }

    suspend fun getImage(isbn: String): ByteArray {
        val fileKey = "${isbn}.jpg"
        val request = GetObjectRequest {
            bucket = BUCKET_NAME
            key = fileKey
        }

        val getObjectResponse = try {
            s3Client.getObject(request) { it }
        } catch (e: NoSuchKey) {
            throw S3FileNotFoundException(BUCKET_NAME, fileKey)
        }

        return getObjectResponse.body!!.toByteArray()
    }
}