package com.example.studentnotes.data.datasources.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentnotes.data.entities.Request

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(request: Request)

    @Update
    fun update(request: Request)

    @Delete
    fun delete(request: Request)

    @Query("select * from request order by request_date")
    fun getAllRequests(): LiveData<List<Request>>
}