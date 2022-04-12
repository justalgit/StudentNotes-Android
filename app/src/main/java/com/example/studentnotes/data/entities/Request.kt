package com.example.studentnotes.data.entities

data class Request(
    var author: String,
    var group: String,
    var message: String? = null
)