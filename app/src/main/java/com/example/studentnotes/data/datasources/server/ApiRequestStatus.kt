package com.example.studentnotes.data.datasources.server

enum class ApiRequestStatus {
    WAITING_TO_SEND,
    LOADING,
    EMPTY,
    HTTP_ERROR,
    TIMEOUT_ERROR,
    UNKNOWN_ERROR,
    DONE
}