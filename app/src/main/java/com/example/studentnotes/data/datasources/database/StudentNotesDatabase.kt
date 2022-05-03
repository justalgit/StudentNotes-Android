package com.example.studentnotes.data.datasources.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentnotes.data.datasources.database.dao.*
import com.example.studentnotes.data.entities.*

@Database(
    entities = [Event::class, User::class, Group::class, Request::class],
    version = 4,
    exportSchema = false
)
abstract class StudentNotesDatabase : RoomDatabase() {

    abstract val eventDao: EventDao
    abstract val userDao: UserDao
    abstract val groupDao: GroupDao
    abstract val requestDao: RequestDao

    companion object {

        @Volatile
        private var INSTANCE: StudentNotesDatabase? = null

        fun getInstance(context: Context): StudentNotesDatabase {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StudentNotesDatabase::class.java,
                        "main_database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}