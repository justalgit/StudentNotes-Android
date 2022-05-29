package com.example.studentnotes.data.datasources.server.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val login: String,
    val password: String
)