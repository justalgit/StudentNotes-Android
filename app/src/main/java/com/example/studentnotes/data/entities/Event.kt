package com.example.studentnotes.data.entities

import java.util.*

data class Event(
    var title: String,
    var description: String,
    var eventDate: Date,
    var creationDate: Date,
    var author: String,
    var group: String
)