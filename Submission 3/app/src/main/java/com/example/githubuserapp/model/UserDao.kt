package com.example.githubuserapp.model

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubuserapp.provider.MappingHelper

@Dao
interface UserDao {

    @Query("DELETE FROM UserFavorite WHERE ${MappingHelper.COLUMN_ID} = :id")
    fun deleteUserFavoriteById(id: Long) : Int

    @Query("SELECT * FROM UserFavorite")
    fun getUserFavoriteList(): List<UserFavorite>

    @Query("SELECT * FROM UserFavorite")
    fun getAllUserFavorites() : Cursor // for content provider

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserFavorites(userFav: UserFavorite) : Long // for content provider

}
