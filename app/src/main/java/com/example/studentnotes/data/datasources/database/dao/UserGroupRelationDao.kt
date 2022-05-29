package com.example.studentnotes.data.datasources.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.studentnotes.data.entities.UserGroupRelation

@Dao
interface UserGroupRelationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userGroupRelation: UserGroupRelation)

    @Insert
    @JvmSuppressWildcards
    fun insertList(objects: List<UserGroupRelation>)

    @Update
    fun update(userGroupRelation: UserGroupRelation)

    @Delete
    fun delete(userGroupRelation: UserGroupRelation)

    @Query("select * from user_group_relation")
    fun getAll(): LiveData<List<UserGroupRelation>>

    @Query("delete from user_group_relation")
    fun clear()

}
