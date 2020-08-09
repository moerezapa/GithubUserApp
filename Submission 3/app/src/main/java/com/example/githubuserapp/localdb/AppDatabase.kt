package com.example.githubuserapp.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuserapp.model.UserDao
import com.example.githubuserapp.model.UserFavorite

// update version if there's change
@Database(entities = [UserFavorite::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getAppDatabase(context: Context) : AppDatabase? {
            if (INSTANCE == null)
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "user_db").build()
                }
            return INSTANCE
        }

        fun destroyDatabase() {
            INSTANCE?.let { INSTANCE = null }
        }
    }

    abstract fun getUserDao() : UserDao
}