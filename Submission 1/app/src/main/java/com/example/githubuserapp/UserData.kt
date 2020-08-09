package com.example.githubuserapp

import android.app.Activity

class UserData(var activity: Activity) {

    private lateinit var name: Array<String>
    private lateinit var companyUser: Array<String>
    private lateinit var locationUser: Array<String>
    private lateinit var username: Array<String>
    private lateinit var repositoryUser: Array<String>
    private lateinit var followers: Array<String>
    private lateinit var following: Array<String>
//    private lateinit var avatar: IntArray

    private fun fetchUserData() {
        name = activity.resources.getStringArray(R.array.name)
        companyUser = activity.resources.getStringArray(R.array.company)
        locationUser = activity.resources.getStringArray(R.array.location)
        username = activity.resources.getStringArray(R.array.username)
        repositoryUser = activity.resources.getStringArray(R.array.repository)
        followers = activity.resources.getStringArray(R.array.followers)
        following = activity.resources.getStringArray(R.array.following)
//        avatar = activity.resources.getIntArray(R.array.avatar)
    }

    private val imageArray = arrayListOf(R.drawable.user1,R.drawable.user2,R.drawable.user3,R.drawable.user4,R.drawable.user5,R.drawable.user6,R.drawable.user7,R.drawable.user8,R.drawable.user9,R.drawable.user10)
    val userListData: ArrayList<User>
    get() {
        fetchUserData()
        val list = arrayListOf<User>()
        for (position in name.indices) {
            list.add(User(
                username[position],
                name[position],
                imageArray[position],
                companyUser[position],
                locationUser[position],
                repositoryUser[position],
                following[position],
                followers[position]
            ))
        }
        return list
    }
}