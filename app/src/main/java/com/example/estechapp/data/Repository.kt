package com.example.estechapp.data

import android.content.Context
import com.example.estechapp.data.models.DataCheckInModel
import com.example.estechapp.data.models.DataEmailModel
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.models.DataMentoringModel
import com.example.estechapp.data.models.DataMentoringModelPatch
import com.example.estechapp.data.network.RetrofitHelper

class Repository(val context: Context) {

    //Este es el repositorio con todos los get y post.

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun postLogin(loginModel: DataLoginModel) = retrofit.login(loginModel)

    suspend fun postEmail(token: String, emailModel: DataEmailModel) =
        retrofit.userInfo(token, emailModel)

    suspend fun postCheckIn(token: String, checkInModel: DataCheckInModel) =
        retrofit.checkIn(token, checkInModel)

    suspend fun getCheckIn(token: String, id: Int) = retrofit.checkInList(token, id)

    suspend fun getMentoringTeacher(token: String, id: Int) = retrofit.mentoringListTeacher(token, id)

    suspend fun getMentoringStudent(token: String, id: Int) = retrofit.mentoringListStudent(token, id)

    suspend fun getRoomList(token: String) = retrofit.roomList(token)

    suspend fun getRoomById(token: String, id: Int) = retrofit.roomById(token, id)

    suspend fun getAllRoles(token: String) = retrofit.roleList(token)

    suspend fun getUserByRole(token: String, role: Int) = retrofit.userByRole(token, role)

    suspend fun patchMentoring(token: String, id: Int, mentoringModel: DataMentoringModelPatch) = retrofit.mentoringPatch(token, id, mentoringModel)

    /*suspend fun getTimeTable(token: String) = retrofit.timeTable(token)*/

}