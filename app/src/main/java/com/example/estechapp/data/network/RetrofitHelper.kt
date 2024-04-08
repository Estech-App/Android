package com.example.estechapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object RetrofitHelper {

    var retrofitService: ApiService? = null

    fun getRetrofit(): ApiService {

        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            retrofitService = retrofit.create(ApiService::class.java)
        }

        return retrofitService!!

    }

}