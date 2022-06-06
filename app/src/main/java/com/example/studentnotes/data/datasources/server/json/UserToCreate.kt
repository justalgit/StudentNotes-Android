package com.example.studentnotes.data.datasources.server.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserToCreate(
    @Json(name = "id") var id: String,
    @Json(name = "name") var name: String,
    @Json(name = "surname") var surname: String,
    @Json(name = "login") var login: String,
    @Json(name = "password") var password: String
)