package com.example.githubuserapp.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("items")
    val userList : ArrayList<User>,
    val followList: ArrayList<User>
)