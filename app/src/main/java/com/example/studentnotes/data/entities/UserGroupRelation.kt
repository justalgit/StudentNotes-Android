package com.example.studentnotes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.studentnotes.utils.replaceDashes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserGroupRelationsList(
    @Json(name = "user_group_relations")
    val availableUserRelations: List<UserGroupRelation>? = emptyList()
)

@Entity(
    tableName = "user_group_relation",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
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
data class UserGroupRelation(
    @ColumnInfo(name = "id") @Json(name = "id") @PrimaryKey
    var id: String,
    @ColumnInfo(name = "user_id") @Json(name = "user_id")
    var userId: String,
    @ColumnInfo(name = "group_id") @Json(name = "group_id")
    var groupId: String
) {
    init {
        id = id.replaceDashes()
        userId = userId.replaceDashes()
        groupId = groupId.replaceDashes()
    }
}
