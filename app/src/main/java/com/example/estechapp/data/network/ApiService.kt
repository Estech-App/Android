package com.example.estechapp.data.network

import com.example.estechapp.data.models.DataCheckInModel
import com.example.estechapp.data.models.DataCheckInResponse
import com.example.estechapp.data.models.DataEmailModel
import com.example.estechapp.data.models.DataFreeUsageModel
import com.example.estechapp.data.models.DataFreeUsageModelPatch
import com.example.estechapp.data.models.DataFreeUsageResponse
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.models.DataLoginResponse
import com.example.estechapp.data.models.DataMentoringModel
import com.example.estechapp.data.models.DataMentoringModelPatch
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.data.models.DataModuleResponse
import com.example.estechapp.data.models.DataRoleResponse
import com.example.estechapp.data.models.DataRoomResponse
import com.example.estechapp.data.models.DataTimeTableResponse
import com.example.estechapp.data.models.DataUserInfoResponse
import com.example.estechapp.data.models.Grupos
import com.example.estechapp.data.models.UserFull
import com.example.estechapp.data.models.UserFullVerdat
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
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

    @Headers("Content-Type: application/json")
    @GET("api/role")
    suspend fun roleList(
        @Header("Authorization") token: String
    ): Response<List<DataRoleResponse>>

    @Headers("Content-Type: application/json")
    @GET("api/user/find-by-role/{role}")
    suspend fun userByRole(
        @Header("Authorization") token: String,
        @Path("role") role: Int
    ): Response<List<UserFull>>

    @Headers("Content-Type: application/json")
    @POST("/api/mentoring")
    suspend fun mentoringPost(
        @Header("Authorization") token: String,
        @Body mentoringModel: DataMentoringModel
    ): Response<DataMentoringResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/api/mentoring/{id}")
    suspend fun mentoringPatch(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body mentoringModel: DataMentoringModelPatch
    ): Response<DataMentoringResponse>

    @Headers("Content-Type: application/json")
    @GET("/api/free-usage/by-student/{id}")
    suspend fun freeUsageList(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<DataFreeUsageResponse>>

    @Headers("Content-Type: application/json")
    @PATCH("/api/free-usage/{id}")
    suspend fun freeUsagePatch(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body freeUsageModel: DataFreeUsageModelPatch
    ): Response<DataFreeUsageResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/free-usage")
    suspend fun freeUsagePost(
        @Header("Authorization") token: String,
        @Body freeUsageModel: DataFreeUsageModel
    ): Response<DataFreeUsageResponse>

    @Headers("Content-Type: application/json")
    @GET("api/user/student/{id}")
    suspend fun getUserStudent(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<UserFullVerdat>

    @Headers("Content-Type: application/json")
    @GET("api/group/by-user/{id}")
    suspend fun getGroupByUser(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<Grupos>>

    @Headers("Content-Type: application/json")
    @GET("/api/time-table/by-group-id/{id}")
    suspend fun timeTableByGroup(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<List<DataTimeTableResponse>>

    @Headers("Content-Type: application/json")
    @GET("/api/time-table")
    suspend fun getTimeTable(
        @Header("Authorization") token: String
    ): Response<List<DataTimeTableResponse>>

    @Headers("Content-Type: application/json")
    @GET("/api/module")
    suspend fun getModules(
        @Header("Authorization") token: String
    ): Response<List<DataModuleResponse>>
}