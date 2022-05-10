package com.example.studentnotes.data.datasources.server.json

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponseBody(
    val isLoggedIn: Boolean
)