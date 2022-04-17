package com.example.studentnotes.utils

const val HEADER_TITLE_LENGTH = 20
const val CARD_DESCRIPTION_LENGTH = 30

fun String.cutOff(limit: Int) =
    if (length > limit)
        slice(0..limit) + "..."
    else
        this