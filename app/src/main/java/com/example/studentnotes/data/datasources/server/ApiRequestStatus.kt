package com.example.studentnotes.data.datasources.server

enum class ApiRequestStatus {
    WAITING_TO_SEND,
    LOADING,
    EMPTY,
    ERROR,
    DONE
}