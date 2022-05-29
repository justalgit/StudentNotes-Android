package com.example.studentnotes.data.datasources.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentnotes.data.entities.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertList(objects: List<Event>)

    @Update
    fun update(event: Event)

    @Delete
    fun delete(event: Event)

    @Query("select * from event order by event_date")
    fun getAll(): LiveData<List<Event>>

    @Query("delete from event")
    fun clear()

}
