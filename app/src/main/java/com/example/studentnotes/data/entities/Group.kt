package com.example.studentnotes.data.entities

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class Group(
    var title: String,
    var description: String,
    var creator: String,
    var isPrivate: Boolean
)

fun Group.toJson(): String {
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(Group::class.java).lenient()
    return jsonAdapter.toJson(this)
}