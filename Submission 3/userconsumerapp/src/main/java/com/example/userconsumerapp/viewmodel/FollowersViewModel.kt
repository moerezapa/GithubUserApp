package com.example.userconsumerapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.userconsumerapp.R
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.network.GetUserAPI
import com.example.userconsumerapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = FollowersViewModel::class.java.simpleName
    private val context = getApplication<Application>().applicationContext
    private val followersList = MutableLiveData<ArrayList<User>>()

    private fun getClientAPI() : GetUserAPI = RetrofitClient.getClient()
    fun getFollowersList(username: String) {
        getClientAPI().getFollowingList(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                    try {
                        response.body()?.let {
                            followersList.postValue(it)
                        }
                    }
                        catch (e: Exception) {
                            Toast.makeText(context, context.resources.getString(R.string.nodata_error_message), Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "onResponse(getFollowersList): Failed to get data because ${e.printStackTrace()}")
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
    fun followersUserList() : LiveData<ArrayList<User>> = followersList
}