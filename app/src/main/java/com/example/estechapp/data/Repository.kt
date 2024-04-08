package com.example.estechapp.data

import android.content.Context
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.network.RetrofitHelper

class Repository(val context: Context) {

    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun postLogin(loginModel: DataLoginModel) = retrofit.login(loginModel)

}