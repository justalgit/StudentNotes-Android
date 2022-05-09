package com.example.studentnotes.utils

const val HEADER_TITLE_LENGTH = 20
const val CARD_DESCRIPTION_LENGTH = 30
const val PREFERENCES_USER_NAME = "PREFERENCES_USER_NAME"
const val PREFERENCES_USER_SURNAME = "PREFERENCES_USER_SURNAME"
var CURRENT_USER_PLACEHOLDER_ID = "2306"

fun String.cutOff(limit: Int) =
    if (length > limit)
        slice(0..limit) + "..."
    else
        this