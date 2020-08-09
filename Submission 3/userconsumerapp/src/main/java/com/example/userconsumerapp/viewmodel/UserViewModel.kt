package com.example.userconsumerapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.userconsumerapp.R
import com.example.userconsumerapp.model.User
import com.example.userconsumerapp.model.UserResponse
import com.example.userconsumerapp.network.GetUserAPI
import com.example.userconsumerapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = UserViewModel::class.java.simpleName
    private val context = getApplication<Application>().applicationContext
    private val userList = MutableLiveData<ArrayList<User>>()
    private val userDetail = MutableLiveData<User>()

    private fun getClientAPI() : GetUserAPI = RetrofitClient.getClient()
    fun searchUsername(username: String) {
        getClientAPI().searchUsername(username)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    try {
                        response.body()?.let {
                            userList.postValue(it.userList)
                        }
                        Log.d(TAG, "onResponse searchUsername: Berhasil")
                    }
                    catch (e: Exception) {
                        Toast.makeText(context, context.resources.getString(R.string.nodata_error_message), Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onResponse searchUsername: Failed to get data because ${e.printStackTrace()}")
                    }
                }
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure searchUsername: Failed to get data on searchUsername()")
                }
            })
    }
    fun getUserDetail(username: String) {
        getClientAPI().getUserDetail(username)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    try {
                        response.body()?.let {
                            userDetail.value = it
                        }
                        Log.d(TAG, "onResponse(getUserDetail): Berhasil")
                    }
                    catch (e: Exception) {
                        Toast.makeText(context, context.resources.getString(R.string.nodata_error_message), Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onResponse(getUserDetail): Failed to get data because ${e.printStackTrace()}")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, "onFailure getUserDetail: Failed to get data on getUserDetail()")
                }
            })
    }
    /**
     * perbedaan MutableLiveData and LiveData
     * MutableLiveData bisa kita ubah value-nya seperti pada userList.postValue(listItems)
     * LiveData bersifat read-only
     */
    fun getUserList() : LiveData<ArrayList<User>> = userList
    fun getUser() : LiveData<User> = userDetail
}