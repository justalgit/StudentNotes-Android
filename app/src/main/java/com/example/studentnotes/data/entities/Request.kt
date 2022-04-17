package com.example.studentnotes.data.entities

import com.squareup.moshi.Moshi

data class Request(
    var author: String,
    var group: String,
    var message: String? = null
)

fun Request.toJson(): String {
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(Request::class.java).lenient()
    return jsonAdapter.toJson(this)
}