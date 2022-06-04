package com.example.studentnotes.data.datasources.database.dao

import androidx.room.*
import com.example.studentnotes.data.entities.Group

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertList(objects: List<Group>)

    @Update
    fun update(group: Group)

    @Delete
    fun delete(group: Group)

    @Query("select * from `group` where id = :groupId")
    fun getById(groupId: String): Group

    @Query("select * from `group` where title = :groupTitle")
    fun getByTitle(groupTitle: String): Group

    @Query("select `group`.* from `group` join user_group_relation on user_group_relation.group_id = `group`.id where user_id = :userId")
    fun getForUser(userId: String) : List<Group>

    @Query("select * from `group` order by title")
    fun getAll(): List<Group>

    @Query("delete from `group`")
    fun clear()

}