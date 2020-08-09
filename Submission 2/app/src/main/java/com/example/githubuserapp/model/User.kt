package com.example.githubuserapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("login")
    var username: String = "",
    var name: String = "",
    var bio: String = "",
    var avatar_url: String = "",
    var company: String = "",
    var location: String = "",
    var public_repos: String = "",
    var following: String = "",
    var followers: String = "",
    var url: String = "",
    var html_url: String = ""
) : Parcelable