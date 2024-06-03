package com.example.estechapp.data.network

import com.example.estechapp.data.models.DataCheckInModel
import com.example.estechapp.data.models.DataCheckInResponse
import com.example.estechapp.data.models.DataEmailModel
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.models.DataLoginResponse
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.data.models.DataRoomResponse
import com.example.estechapp.data.models.DataTimeTableModel
import com.example.estechapp.data.models.DataTimeTableResponse
import com.example.estechapp.data.models.DataUserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    //Aqui estan todos los get y post.

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(
        @Body loginModel: DataLoginModel
    ): Response<DataLoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/user/user-info")
    suspend fun userInfo(
        @Header("Authorization") token: String,
        @Body emailModel: DataEmailModel
    ): Response<DataUserInfoResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/check-in/new")
    suspend fun checkIn(
        @Header("Authorization") token: String,
        @Body checkInModel: DataCheckInModel
    ): Response<DataCheckInResponse>

    @Headers("Content-Type: application/json")
    @GET("/api/check-in/by-user/{id}")
    suspend fun checkInList(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<DataCheckInResponse>>

    @Headers("Content-Type: application/json")
    @GET("/api/mentoring/by-teacher/{id}")
    suspend fun mentoringListTeacher(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<DataMentoringResponse>>

    @Headers("Content-Type: application/json")
    @GET("/api/mentoring/by-student/{id}")
    suspend fun mentoringListStudent(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<DataMentoringResponse>>

    @Headers("Content-Type: application/json")
    @GET("/api/room")
    suspend fun roomList(
        @Header("Authorization") token: String,
    ): Response<List<DataRoomResponse>>

    @Headers("Content-Type: application/json")
    @GET("/api/room/{id}")
    suspend fun roomById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<DataRoomResponse>

    /*@Headers("Content-Type: application/json")
    @GET("/api/time-table")
    suspend fun timeTable(
        @Header("Authorization") token: String
    ) : Response<DataTimeTableResponse>*/
}