package com.example.estechapp.data.network

import com.example.estechapp.data.models.DataEmailModel
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.models.DataLoginResponse
import com.example.estechapp.data.models.DataUserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(
        @Body loginModel: DataLoginModel
    ) : Response<DataLoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/user/user-info")
    suspend fun userInfo(
        @Header("Authorization") token: String,
        @Body emailModel: DataEmailModel
    ) : Response<DataUserInfoResponse>

}