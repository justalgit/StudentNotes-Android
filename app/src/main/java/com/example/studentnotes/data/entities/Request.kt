package com.example.studentnotes.data.entities

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.studentnotes.R
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.utils.replaceDashes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

enum class RequestType {
    OUTCOMING,
    INCOMING,
}

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
        groupId = groupId.replaceDashes()
    }
}

fun Request.requestType(currentUserId: String): RequestType {
    return if (authorId != currentUserId)
        RequestType.INCOMING
    else
        RequestType.OUTCOMING
}

fun Request.requestTypeStringValue(context: Context, currentUserId: String): String {
    val requestType = requestType(currentUserId)
    return when (requestType) {
        RequestType.INCOMING -> context.getString(R.string.incoming_request)
        RequestType.OUTCOMING -> context.getString(R.string.outcoming_request)
    }
}

fun Request.RequestTypeTextMessage(
    currentUserId: String,
    context: Context,
    databaseRepo: DatabaseRepository
): String {
    return when (this.requestType(currentUserId)) {
        RequestType.INCOMING -> context.getString(
            R.string.user_wants_to_join_group,
            authorId,
            groupId
        )
        RequestType.OUTCOMING -> context.getString(
            R.string.you_sent_request_to_join_group,
            groupId
        )
    }
}
