package com.example.studentnotes.data.datasources.database.dao

import androidx.room.*
import com.example.studentnotes.data.entities.Request

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(request: Request)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertList(objects: List<Request>)

    @Update
    fun update(request: Request)

    @Delete
    fun delete(request: Request)

    @Query("select * from request where id = :requestId")
    fun getById(requestId: String): Request

    @Query("select * from request order by request_date")
    fun getAll(): List<Request>

    @Query("delete from request")
    fun clear()

}