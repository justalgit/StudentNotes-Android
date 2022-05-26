package com.example.studentnotes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "user"
)
@JsonClass(generateAdapter = true)
data class User(
    @ColumnInfo(name = "id") @Json(name = "id") @PrimaryKey var id: String,
    @ColumnInfo(name = "name") @Json(name = "name") var name: String,
    @ColumnInfo(name = "surname") @Json(name = "surname") var surname: String
)