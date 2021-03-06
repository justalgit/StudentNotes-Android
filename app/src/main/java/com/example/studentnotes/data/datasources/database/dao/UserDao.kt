package com.example.studentnotes.data.datasources.database.dao

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
    fun getById(userId: String): User

    @Query("select * from user where name like :name and surname like :surname")
    fun getByNameAndSurname(name: String, surname: String): User

    @Query("select * from user")
    fun getAll(): List<User>

    @Query("delete from user")
    fun clear()

}