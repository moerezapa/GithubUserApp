package com.example.githubuserapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private lateinit var retrofit : Retrofit
        private var baseEndpoint = "https://api.github.com/"
        const val personalToken = "cc57654b0729a578f10c276906f7d79049082ae9"
        fun getClient() : GetUserAPI {
            retrofit = Retrofit.Builder()
                .baseUrl(baseEndpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(GetUserAPI::class.java)
        }
    }

}