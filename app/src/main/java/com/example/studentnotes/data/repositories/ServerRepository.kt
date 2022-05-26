package com.example.studentnotes.data.repositories

import androidx.lifecycle.LiveData
import com.example.studentnotes.data.datasources.server.StudentNotesApi
import com.example.studentnotes.data.datasources.server.json.LoginRequestBody
import com.example.studentnotes.data.datasources.server.json.LoginResponseBody
import com.example.studentnotes.data.entities.GroupsList

class ServerRepository {

    suspend fun login(login: String, password: String): LoginResponseBody {
        return StudentNotesApi.retrofitService.login(
            LoginRequestBody(login, password)
        )
    }

    suspend fun getUserGroups(userId: String): GroupsList {
        return StudentNotesApi.retrofitService.getUserGroups(userId)
    }

}