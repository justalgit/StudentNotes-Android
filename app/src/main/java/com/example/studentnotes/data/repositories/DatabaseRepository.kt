package com.example.studentnotes.data.repositories

import androidx.lifecycle.LiveData
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.datasources.server.json.InitialDataResponse
import com.example.studentnotes.data.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(private val database: StudentNotesDatabase) {

    fun getGroupById(groupId: String): Group {
        return database.groupDao.getById(groupId)
    }

    fun getUserById(userId: String): User {
        return database.userDao.getById(userId)
    }

    fun getEventById(eventId: String): Event {
        return database.eventDao.getById(eventId)
    }

    fun getRequestById(requestId: String): Request {
        return database.requestDao.getById(requestId)
    }

    fun getGroupByTitle(groupTitle: String): Group {
        return database.groupDao.getByTitle(groupTitle)
    }

    fun getUserByNameAndSurname(name: String, surname: String): User {
        return database.userDao.getByNameAndSurname(name, surname)
    }

    fun getAllUsers(): LiveData<List<User>> {
        return database.userDao.getAll()
    }

    fun getAllEvents(): LiveData<List<Event>> {
        return database.eventDao.getAll()
    }

    fun getAllGroups(): LiveData<List<Group>> {
        return database.groupDao.getAll()
    }

    fun getAllRequests(): LiveData<List<Request>> {
        return database.requestDao.getAll()
    }

    fun insertInitialData(initialData: InitialDataResponse) {
        with(initialData) {
            insertUsersList(usersList)
            insertGroupsList(groupsList)
            insertUserGroupRelationsList(userGroupRelationsList)
            insertEventsList(eventsList)
            insertRequestsList(requestsList)
        }
    }

    fun clearData() {
        with(database) {
            requestDao.clear()
            eventDao.clear()
            userGroupRelationDao.clear()
            groupDao.clear()
            userDao.clear()
        }
    }

    fun insertUsersList(users: List<User>) {
        database.userDao.insertList(users)
    }

    fun insertGroupsList(groups: List<Group>) {
        database.groupDao.insertList(groups)
    }

    fun insertEventsList(events: List<Event>) {
        database.eventDao.insertList(events)
    }

    fun insertRequestsList(requests: List<Request>) {
        database.requestDao.insertList(requests)
    }

    fun insertUserGroupRelationsList(userGroupRelations: List<UserGroupRelation>) {
        database.userGroupRelationDao.insertList(userGroupRelations)
    }

    suspend fun insertEvent(event: Event) {
        withContext(Dispatchers.IO) {
            database.eventDao.insert(event)
        }
    }

    suspend fun updateEvent(event: Event) {
        withContext(Dispatchers.IO) {
            database.eventDao.update(event)
        }
    }

    suspend fun deleteEvent(event: Event) {
        withContext(Dispatchers.IO) {
            database.eventDao.delete(event)
        }
    }

    suspend fun insertGroup(group: Group) {
        withContext(Dispatchers.IO) {
            database.groupDao.insert(group)
        }
    }

    suspend fun updateGroup(group: Group) {
        withContext(Dispatchers.IO) {
            database.groupDao.update(group)
        }
    }

    suspend fun deleteGroup(group: Group) {
        withContext(Dispatchers.IO) {
            database.groupDao.delete(group)
        }
    }

    suspend fun insertRequest(request: Request) {
        withContext(Dispatchers.IO) {
            database.requestDao.insert(request)
        }
    }

    suspend fun updateRequest(request: Request) {
        withContext(Dispatchers.IO) {
            database.requestDao.update(request)
        }
    }

    suspend fun deleteRequest(request: Request) {
        withContext(Dispatchers.IO) {
            database.requestDao.delete(request)
        }
    }
}