package com.example.studentnotes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.studentnotes.utils.replaceDashes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@JsonClass(generateAdapter = true)
data class RequestsList(
    @Json(name = "requests")
    val requests: List<Request>? = emptyList()
)

@Entity(
    tableName = "request",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("author_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("incoming_user_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Group::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("group_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
@JsonClass(generateAdapter = true)
data class Request(
    @ColumnInfo(name = "id") @Json(name = "id") @PrimaryKey
    var id: String,
    @ColumnInfo(name = "author_id") @Json(name = "author_id")
    var authorId: String,
    @ColumnInfo(name = "incoming_user_id") @Json(name = "incoming_user_id")
    var incomingUserId: String,
    @ColumnInfo(name = "group_id") @Json(name = "group_id")
    var groupId: String,
    @ColumnInfo(name = "request_date") @Json(name = "request_date")
    var requestDate: Long,
    @ColumnInfo(name = "message") @Json(name = "message")
    var message: String? = null
) {
    init {
        id = id.replaceDashes()
        authorId = authorId.replaceDashes()
        incomingUserId = incomingUserId.replaceDashes()
        groupId = groupId.replaceDashes()
    }
}


fun Request.toJson(): String {
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(Request::class.java).lenient()
    return jsonAdapter.toJson(this)
}

fun RequestsList.toJson(): String {
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(RequestsList::class.java).lenient()
    return jsonAdapter.toJson(this)
}