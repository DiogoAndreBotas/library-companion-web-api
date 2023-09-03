package com.diogoandrebotas.librarycompanionwebapi.exception

class S3FileNotFoundException(bucketName: String, fileKey: String)
    : Exception("File with key $fileKey not found in S3 bucket $bucketName")