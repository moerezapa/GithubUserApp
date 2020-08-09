package com.example.githubuserapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuserapp.R
import com.example.githubuserapp.model.User
import com.example.githubuserapp.network.GetUserAPI
import com.example.githubuserapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = FollowingViewModel::class.java.simpleName
    private val context = getApplication<Application>().applicationContext
    private val followingList = MutableLiveData<ArrayList<User>>()

    private fun getClientAPI() : GetUserAPI = RetrofitClient.getClient()
    fun getFollowingList(username: String) {
        getClientAPI().getFollowingList(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                    try {
                        response.body()?.let {
                            followingList.postValue(it)
                        }
                    }
                        catch (e: Exception) {
                            Toast.makeText(context, context.resources.getString(R.string.nodata_error_message), Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "onResponse(getFollowingList): Failed to get data because ${e.printStackTrace()}")
                        }
                }
                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d(TAG, "onFailure: Failed to get data on getFollowingList()")
                }
            })
    }

    /**
     * perbedaan MutableLiveData and LiveData
     * MutableLiveData bisa kita ubah value-nya seperti pada userList.postValue(listItems)
     * LiveData bersifat read-only
     */
    fun followingUserList() : LiveData<ArrayList<User>> = followingList
}