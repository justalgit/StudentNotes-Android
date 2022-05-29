package com.example.studentnotes.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studentnotes.utils.replaceDashes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@JsonClass(generateAdapter = true)
data class UsersList(
    @Json(name = "users")
    val users: List<User>? = emptyList()
)

@Entity(
    tableName = "user"
)
@JsonClass(generateAdapter = true)
data class User(
    @ColumnInfo(name = "id") @Json(name = "id") @PrimaryKey
    var id: String,
    @ColumnInfo(name = "name") @Json(name = "name")
    var name: String,
    @ColumnInfo(name = "surname") @Json(name = "surname")
    var surname: String
) {
    init {
        id = id.replaceDashes()
    }
}

fun User.toJson(): String {
    val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val jsonAdapter = moshi.adapter(User::class.java).lenient()
    return jsonAdapter.toJson(this)
}