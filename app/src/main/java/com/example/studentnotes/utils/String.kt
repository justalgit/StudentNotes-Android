package com.example.studentnotes.utils

const val HEADER_TITLE_LENGTH = 20
const val CARD_DESCRIPTION_LENGTH = 30
var CURRENT_USER_PLACEHOLDER_ID = "2306"

fun String.cutOff(limit: Int) =
    if (length > limit)
        slice(0..limit) + "..."
    else
        this