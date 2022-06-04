package com.example.studentnotes.data.repositories

import android.util.Log
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.datasources.server.json.InitialDataResponse
import com.example.studentnotes.data.entities.*

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

    fun getUserGroups(userId: String): List<Group> {
        return database.groupDao.getForUser(userId)
    }

    fun getGroupsWithoutUser(userId: String): List<Group> {
        val allGroups = database.groupDao.getAll()
        Log.d("allGroupsIds", allGroups.map { it.id }.toString())
        val groupsForUserIds = database.groupDao.getForUser(userId).map { it.id }
        Log.d("groupsForUserIds", groupsForUserIds.toString())
        Log.d("resultIds", allGroups.filter { it.id !in groupsForUserIds }.map {it.id}.toString())
        return allGroups.filter { it.id !in groupsForUserIds }
    }

    fun getAllUsers(): List<User> {
        return database.userDao.getAll()
    }

    fun getAllEvents(): List<Event> {
        return database.eventDao.getAll()
    }

    fun getAllGroups(): List<Group> {
        return database.groupDao.getAll()
    }

    fun getAllRequests(): List<Request> {
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

    private fun insertUsersList(users: List<User>) {
        database.userDao.insertList(users)
    }

    private fun insertGroupsList(groups: List<Group>) {
        database.groupDao.insertList(groups)
    }

    private fun insertEventsList(events: List<Event>) {
        database.eventDao.insertList(events)
    }

    private fun insertRequestsList(requests: List<Request>) {
        database.requestDao.insertList(requests)
    }

    private fun insertUserGroupRelationsList(userGroupRelations: List<UserGroupRelation>) {
        database.userGroupRelationDao.insertList(userGroupRelations)
    }
}