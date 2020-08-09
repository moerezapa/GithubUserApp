package com.example.userconsumerapp.model

import android.content.ContentValues
import android.os.Parcelable
import android.provider.BaseColumns
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity
data class UserFavorite(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("login")
    var username: String = "",
    var name: String = "",
    var avatar_url: String = "",
    var public_repos: String = ""
) : Parcelable {

    companion object {
        /** The name of the Menu table.  */
        const val TABLE_NAME = "UserFavorite"

        /** The name of the ID column.  */
        const val COLUMN_ID = BaseColumns._ID

        fun fromContentValues(contentValues: ContentValues): UserFavorite {
            val userFavorite = UserFavorite()
            if (contentValues.containsKey(COLUMN_ID))
                userFavorite.id = contentValues.getAsInteger(COLUMN_ID)
            if (contentValues.containsKey(userFavorite.username))
                userFavorite.username = contentValues.getAsString(userFavorite.username)
            if (contentValues.containsKey(userFavorite.name))
                userFavorite.name = contentValues.getAsString(userFavorite.name)
            if (contentValues.containsKey(userFavorite.public_repos))
                userFavorite.public_repos = contentValues.getAsString(userFavorite.public_repos)
            if (contentValues.containsKey(userFavorite.avatar_url))
                userFavorite.avatar_url = contentValues.getAsString(userFavorite.avatar_url)
            return userFavorite
        }
    }
}