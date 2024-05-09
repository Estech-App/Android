package com.example.estechapp.data

import android.content.Context
import com.example.estechapp.data.models.DataCheckInModel
import com.example.estechapp.data.models.DataEmailModel
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.models.DataTimeTableModel
import com.example.estechapp.data.network.RetrofitHelper

class Repository(val context: Context) {

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun postLogin(loginModel: DataLoginModel) = retrofit.login(loginModel)

    suspend fun postEmail(token: String, emailModel: DataEmailModel) =
        retrofit.userInfo(token, emailModel)

    suspend fun postCheckIn(token: String, checkInModel: DataCheckInModel) =
        retrofit.checkIn(token, checkInModel)

    suspend fun getCheckIn(token: String, id: Int) = retrofit.checkInList(token, id)

    /*suspend fun getTimeTable(token: String) = retrofit.timeTable(token)*/

}