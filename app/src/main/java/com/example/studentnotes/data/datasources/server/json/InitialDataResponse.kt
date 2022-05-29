package com.example.studentnotes.data.datasources.server.json

import com.example.studentnotes.data.entities.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InitialDataResponse(
    @Json(name = "users") var usersList: List<User>,
    @Json(name = "groups") var groupsList: List<Group>,
    @Json(name = "events") var eventsList: List<Event>,
    @Json(name = "requests") var requestsList: List<Request>,
    @Json(name = "user_group_relations") var userGroupRelationsList: List<UserGroupRelation>
)