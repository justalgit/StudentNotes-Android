package com.example.studentnotes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity(
    tableName = "event",
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
            childColumns = arrayOf("last_modified_user_id"),
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
data class Event(
    @ColumnInfo(name = "id") @Json(name = "id") @PrimaryKey var id: String,
    @ColumnInfo(name = "title") @Json(name = "title") var title: String,
    @ColumnInfo(name = "description") @Json(name = "description") var description: String? = null,
    @ColumnInfo(name = "event_date") @Json(name = "event_date") var eventDate: Long,
    @ColumnInfo(name = "author_id") @Json(name = "author_id") var authorId: String,
    @ColumnInfo(name = "last_modified_date") @Json(name = "last_modified_date") var lastModifiedDate: Long,
    @ColumnInfo(name = "last_modified_user_id") @Json(name = "last_modified_user_id") var lastModifiedUserId: String,
    @ColumnInfo(name = "group_id") @Json(name = "group_id") var groupId: String,
    @ColumnInfo(name = "is_editable") @Json(name = "is_editable") var isEditable: Boolean = true
)


fun Event.toJson(): String {
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(Event::class.java).lenient()
    return jsonAdapter.toJson(this)
}