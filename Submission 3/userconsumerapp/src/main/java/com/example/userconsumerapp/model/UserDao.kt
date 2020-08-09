package com.example.userconsumerapp.model

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.userconsumerapp.provider.MappingHelper

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorite(user: UserFavorite)

    @Query("DELETE FROM UserFavorite WHERE ${MappingHelper.COLUMN_ID} = :id")
    fun deleteUserFavoriteById(id: Long) : Int

    @androidx.room.Query("SELECT * FROM UserFavorite WHERE username == :username")
    fun getFavoriteByName(username: String): List<UserFavorite>

    @Query("SELECT * FROM UserFavorite")
    fun getUserFavoriteList(): List<UserFavorite>

    @Query("SELECT * FROM UserFavorite")
    fun getAllUserFavorites() : Cursor // for content provider

    @Insert
    fun insertUserFavorites(userFav: UserFavorite) : Long // for content provider

}
