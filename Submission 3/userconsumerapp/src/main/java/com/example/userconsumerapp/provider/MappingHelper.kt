package com.example.userconsumerapp.provider

import android.database.Cursor
import com.example.userconsumerapp.model.UserFavorite

object MappingHelper {

    /** The name of the table column. <UserFavourite>  */
    const val COLUMN_ID = "id"
    const val USERNAME = "username"
    const val NAME = "name"
    const val AVATAR_URL = "avatar_url"
    const val PUBLIC_REPOS = "public_repos"

    fun mapCursorToUserArrayList(cursor: Cursor?) : ArrayList<UserFavorite> {
        val userFavList = ArrayList<UserFavorite>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
                val publicRepos = getString(getColumnIndexOrThrow(PUBLIC_REPOS))
                userFavList.add(UserFavorite(id, username, name, avatarUrl, publicRepos))
            }
        }
        return userFavList
    }

    fun mapCursorToUserObject(cursor: Cursor?) : UserFavorite {
        var userFavorite = UserFavorite()
        cursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
            val username = getString(getColumnIndexOrThrow(USERNAME))
            val name = getString(getColumnIndexOrThrow(NAME))
            val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
            val publicRepos = getString(getColumnIndexOrThrow(PUBLIC_REPOS))
            userFavorite = UserFavorite(id, username, name, avatarUrl, publicRepos)
        }
        return userFavorite
    }
}