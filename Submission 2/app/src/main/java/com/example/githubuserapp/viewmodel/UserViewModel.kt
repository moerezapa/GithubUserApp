package com.example.githubuserapp.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.model.User
import com.example.githubuserapp.model.UserResponse
import com.example.githubuserapp.network.GetUserAPI
import com.example.githubuserapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val TAG = UserViewModel::class.java.simpleName
    private val userList = MutableLiveData<ArrayList<User>>()
    private val followersList = MutableLiveData<ArrayList<User>>()
    private val followingList = MutableLiveData<ArrayList<User>>()
    private val userDetail = MutableLiveData<User>()

    private fun getClientAPI() : GetUserAPI = RetrofitClient.getClient()
    fun searchUsername(context: Context, username: String) {
        getClientAPI().searchUsername(username)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    try {
                        response.body()?.let {
                            userList.postValue(it.userList)
                        }
                        Log.d(TAG, "onResponse: Berhasil")
                    }
                    catch (e: Exception) {
                        Toast.makeText(context, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onResponse: Failed to get data because ${e.printStackTrace()}")
                    }
                }
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: Failed to get data on searchUsername()")
                }
            })
    }
    fun getUserDetail(username: String) {
        getClientAPI().getUserDetail(username)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    response.body()?.let {
                        userDetail.value = it
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, "onFailure: Failed to get data on getUserDetail()")
                }
            })
    }
    fun getFollowingList(context: Context, username: String) {
        getClientAPI().getFollowingList(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                    try {
                        response.body()?.let {
                            followingList.postValue(it)
                        }
                    }
                        catch (e: Exception) {
                            Toast.makeText(context, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "onResponse(getFollowingList): Failed to get data because ${e.printStackTrace()}")
                        }
                }
                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d(TAG, "onFailure: Failed to get data on getFollowingList()")
                }
            })
    }
    fun getFollowersList(context: Context, username: String) {
        getClientAPI().getFollowersList(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                    try {
                        response.body()?.let {
                            followersList.postValue(it)
                        }
                    }
                    catch (e: Exception) {
                        Toast.makeText(context, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onResponse(getFollowersList): Failed to get data because ${e.printStackTrace()}")
                    }
                }
                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d(TAG, "onFailure: Failed to get data on getFollowersList()")
                }
            })
    }
    /**
     * perbedaan MutableLiveData and LiveData
     * MutableLiveData bisa kita ubah value-nya seperti pada userList.postValue(listItems)
     * LiveData bersifat read-only
     */
    fun getUserList() : LiveData<ArrayList<User>> = userList
    fun followingUserList() : LiveData<ArrayList<User>> = followingList
    fun followersUserList() : LiveData<ArrayList<User>> = followersList
    fun getUser() : LiveData<User> = userDetail
}