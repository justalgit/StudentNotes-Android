package com.example.studentnotes.data.datasources.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentnotes.data.entities.Group

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: Group)

    @Update
    fun update(group: Group)

    @Delete
    fun delete(group: Group)

    @Query("select * from `group` where title = :groupTitle")
    fun getByTitle(groupTitle: String): Group

    @Query("select * from `group` order by title")
    fun getAllGroups(): LiveData<List<Group>>
}