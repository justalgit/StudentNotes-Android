package com.example.studentnotes.data.repositories

import com.example.studentnotes.data.datasources.server.StudentNotesApi
import com.example.studentnotes.data.datasources.server.json.InitialDataResponse
import com.example.studentnotes.data.datasources.server.json.LoginRequest
import com.example.studentnotes.data.datasources.server.json.LoginResponse
import com.example.studentnotes.data.datasources.server.json.UserToCreate
import com.example.studentnotes.data.entities.*

class ServerRepository {

    suspend fun createUser(userToCreate: UserToCreate) {
        return StudentNotesApi.retrofitService.createUser(userToCreate)
    }

    suspend fun login(login: String, password: String): LoginResponse {
        return StudentNotesApi.retrofitService.login(
            LoginRequest(login, password)
        )
    }

    suspend fun getInitialData(userId: String): InitialDataResponse {
        return StudentNotesApi.retrofitService.getInitialData(userId)
    }

    suspend fun getAllGroups(): GroupsList {
        return StudentNotesApi.retrofitService.getAllGroups()
    }

    suspend fun getAllUsers(): UsersList {
        return StudentNotesApi.retrofitService.getAllUsers()
    }

    suspend fun checkEvent(eventId: String, userId: String) {
        return StudentNotesApi.retrofitService.checkEvent(eventId, userId)
    }

    suspend fun changeEventPriority(eventId: String, userId: String, priority: Int) {
        return StudentNotesApi.retrofitService.changeEventPriority(eventId, userId, priority)
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

    suspend fun createEvent(event: Event) {
        return StudentNotesApi.retrofitService.createEvent(event)
    }

    suspend fun updateEvent(event: Event) {
        return StudentNotesApi.retrofitService.updateEvent(event, event.id)
    }

    suspend fun deleteEvent(eventId: String) {
        return StudentNotesApi.retrofitService.deleteEvent(eventId)
    }

    suspend fun createRequest(request: Request) {
        return StudentNotesApi.retrofitService.createRequest(request)
    }

    suspend fun deleteRequest(requestId: String, isAccept: Boolean) {
        return StudentNotesApi.retrofitService.deleteRequest(requestId, isAccept)
    }

    suspend fun leaveGroup(groupId: String, userId: String) {
        return StudentNotesApi.retrofitService.leaveGroup(groupId, userId)
    }

}