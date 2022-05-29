package com.example.studentnotes.data.repositories

import com.example.studentnotes.data.datasources.server.StudentNotesApi
import com.example.studentnotes.data.datasources.server.json.InitialDataResponse
import com.example.studentnotes.data.datasources.server.json.LoginRequest
import com.example.studentnotes.data.datasources.server.json.LoginResponse
import com.example.studentnotes.data.entities.Group

class ServerRepository {

    suspend fun login(login: String, password: String): LoginResponse {
        return StudentNotesApi.retrofitService.login(
            LoginRequest(login, password)
        )
    }

    suspend fun getInitialData(userId: String): InitialDataResponse {
        return StudentNotesApi.retrofitService.getInitialData(userId)
    }

    suspend fun createGroup(group: Group) {
        return StudentNotesApi.retrofitService.createGroup(group)
    }

    suspend fun updateGroup(group: Group) {
        return StudentNotesApi.retrofitService.updateGroup(group, group.id)
    }

    suspend fun deleteGroup(groupId: String) {
        return StudentNotesApi.retrofitService.deleteGroup(groupId)
    }

}