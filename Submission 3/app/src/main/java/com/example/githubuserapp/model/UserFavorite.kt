package com.example.githubuserapp.model

import android.os.Parcelable
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
) : Parcelable