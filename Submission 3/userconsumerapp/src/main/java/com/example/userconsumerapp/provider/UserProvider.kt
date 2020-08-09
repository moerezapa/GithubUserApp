package com.example.userconsumerapp.provider

import android.content.UriMatcher
import android.net.Uri
import com.example.userconsumerapp.model.UserFavorite

class UserProvider  {

    companion object {
        private val TAG = UserProvider::class.java.simpleName
        private const val USER = 1
        private const val USERFAV_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        private const val AUTHORITY = "com.example.githubuserapp"
        private val tableName = UserFavorite::class.java.simpleName
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$tableName")

        init {
            // content://"com.example.githubuserapp/note
            sUriMatcher.addURI(AUTHORITY, tableName, USER)
            // content://"com.example.githubuserapp/note/id
            sUriMatcher.addURI(AUTHORITY, "$tableName/#", USERFAV_ID)
        }
    }
}