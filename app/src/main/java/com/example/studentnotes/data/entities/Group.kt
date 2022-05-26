package com.example.studentnotes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@JsonClass(generateAdapter = true)
data class GroupsList(
    @Json(name = "groups") val groups: List<Group>? = emptyList()
)

@Entity(
    tableName = "group",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("creator_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("last_modified_user_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
@JsonClass(generateAdapter = true)
data class Group(
    @ColumnInfo(name = "id") @Json(name = "id") @PrimaryKey var id: String,
    @ColumnInfo(name = "title") @Json(name = "title") var title: String,
    @ColumnInfo(name = "description") @Json(name = "description") var description: String? = null,
    @ColumnInfo(name = "creator_id") @Json(name = "creator_id") var creatorId: String,
    @ColumnInfo(name = "last_modified_date") @Json(name = "last_modified_date") var lastModifiedDate: Long,
    @ColumnInfo(name = "last_modified_user_id") @Json(name = "last_modified_user_id") var lastModifiedUserId: String,
    @ColumnInfo(name = "is_private") @Json(name = "is_private") var isPrivate: Boolean,
    @ColumnInfo(name = "is_editable") @Json(name = "is_editable") var isEditable: Boolean
)


fun Group.toJson(): String {
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(Group::class.java).lenient()
    return jsonAdapter.toJson(this)
}