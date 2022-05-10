package com.example.studentnotes.data.repositories

import com.example.studentnotes.data.datasources.server.StudentNotesApi
import com.example.studentnotes.data.datasources.server.json.LoginRequestBody

class ServerRepository {

    suspend fun login(login: String, password: String) =
        StudentNotesApi.retrofitService.login(
            LoginRequestBody(login, password)
        )

}