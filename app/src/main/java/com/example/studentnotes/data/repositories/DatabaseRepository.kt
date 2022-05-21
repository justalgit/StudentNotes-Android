package com.example.studentnotes.data.repositories

import androidx.lifecycle.LiveData
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.data.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(private val database: StudentNotesDatabase) {

    suspend fun createUser(user: User) {
        withContext(Dispatchers.IO) {
            database.userDao.insert(user)
        }
    }

    fun getGroupByTitle(groupTitle: String): Group {
        return database.groupDao.getByTitle(groupTitle)
    }

    fun getAllEvents(): LiveData<List<Event>> {
        return database.eventDao.getAllEvents()
    }

    fun getAllGroups(): LiveData<List<Group>> {
        return database.groupDao.getAllGroups()
    }

    fun getAllRequests(): LiveData<List<Request>> {
        return database.requestDao.getAllRequests()
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