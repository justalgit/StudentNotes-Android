package com.example.studentnotes.data.datasources.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentnotes.data.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertList(objects: List<User>)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("select * from user where id = :userId")
    fun getById(userId: String): LiveData<User>

    @Query("delete from user")
    fun clear()

}