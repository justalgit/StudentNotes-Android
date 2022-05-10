package com.example.studentnotes.data.datasources.server.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestBody(
    val login: String,
    val password: String
)