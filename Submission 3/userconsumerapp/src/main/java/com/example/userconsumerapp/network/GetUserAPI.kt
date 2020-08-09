package com.example.userconsumerapp.network

import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GetUserAPI {

    @GET("/search/users")
    @Headers("Authorization: token ${RetrofitClient.personalToken}")
    fun searchUsername(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("/users/{username}")
    @Headers("Authorization: token ${RetrofitClient.personalToken}")
    fun getUserDetail(
        @Path("username") username: String
    ) : Call<User>

    @GET("/users/{username}/following")
    @Headers("Authorization: token ${RetrofitClient.personalToken}")
    fun getFollowingList(
        @Path("username") username: String
    ) : Call<ArrayList<User>>

    @GET("/users/{username}/followers")
    @Headers("Authorization: token ${RetrofitClient.personalToken}")
    fun getFollowersList(
        @Path("username") username: String
    ) : Call<ArrayList<User>>
}