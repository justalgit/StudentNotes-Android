package com.example.studentnotes.data.entities

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class Event(
    var title: String,
    var description: String,
    var eventDate: Long,
    var author: String,
    var lastModifiedDate: Long,
    var lastModifiedUser: String,
    var group: String,
    var isEditable: Boolean = true
)

fun Event.toJson(): String {
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(Event::class.java).lenient()
    return jsonAdapter.toJson(this)
}