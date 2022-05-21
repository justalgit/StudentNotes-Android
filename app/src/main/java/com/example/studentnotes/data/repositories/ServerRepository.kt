package com.example.studentnotes.data.repositories

import com.example.studentnotes.data.datasources.server.StudentNotesApi
import com.example.studentnotes.data.datasources.server.json.LoginRequestBody
import com.example.studentnotes.data.datasources.server.json.LoginResponseBody

class ServerRepository {

    suspend fun login(login: String, password: String): LoginResponseBody {
        return StudentNotesApi.retrofitService.login(
            LoginRequestBody(login, password)
        )
    }

}