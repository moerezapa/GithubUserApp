package com.example.githubuserapp.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.githubuserapp.localdb.AppDatabase
import com.example.githubuserapp.model.UserDao
import com.example.githubuserapp.model.UserFavorite
import java.util.*

class UserProvider : ContentProvider() {

    companion object {
        private val TAG = UserProvider::class.java.simpleName
        private const val USER = 1
        private const val USERFAV_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userDao: UserDao

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

    // initialise the provider
    override fun onCreate(): Boolean {
        val appDatabase = context?.let { AppDatabase.getAppDatabase(it) }
        appDatabase?.let {
            userDao = it.getUserDao()
        }
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor?  {
        Log.d(TAG, "query: ")
        val cursor : Cursor?
        when(sUriMatcher.match(uri)) {
            USER -> {
                cursor = userDao.getAllUserFavorites()
                context?.let {
                    Log.d(TAG, "query on USER: ")
                    Objects.requireNonNull(cursor).setNotificationUri(it.contentResolver, uri)
                    return cursor
                }
            }
            else -> return null
        }

        return cursor
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert: ")
        val added: Long =
            when(USER) {
                sUriMatcher.match(uri)  -> userDao.insertUserFavorites(MappingHelper.fromContentValues(values!!))
                else -> 0
            }
        Log.d(TAG, "insert succesfully on $uri with id: $added")
        // memberitahu kalau ada perubahan data & ngirim pesan ke semua aplikasi yg ngakses data dari content provider ini
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(sUriMatcher.match(uri)) {
            USER -> throw IllegalArgumentException("Invalid URI. Please select an ID")
            USERFAV_ID -> {
                val context = context ?: return 0
                val count = userDao.deleteUserFavoriteById(ContentUris.parseId(uri))
                Log.d(TAG, "delete succesfully on URI: $uri with id: $count")
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI")
        }
    }
}