package com.example.estechapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.estechapp.data.Repository
import com.example.estechapp.data.models.DataCheckInModel
import com.example.estechapp.data.models.DataCheckInResponse
import com.example.estechapp.data.models.DataEmailModel
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.models.DataLoginResponse
import com.example.estechapp.data.models.DataUserInfoResponse
import com.example.estechapp.data.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(val context: Context) : ViewModel() {

    private val repository = Repository(context)

    val liveDataLogin = MutableLiveData<DataLoginResponse>()
    val liveDataLoginError = MutableLiveData<String>()
    val liveDataUserInfo = MutableLiveData<DataUserInfoResponse>()
    val liveDataUserInfoError = MutableLiveData<String>()
    val liveDataCheckIn = SingleLiveEvent<DataCheckInResponse>()
    val liveDataCheckInError = SingleLiveEvent<String>()
    val liveDataCheckInList = MutableLiveData<List<DataCheckInResponse>>()
    //val liveDataTimeTable = MutableLiveData<DataTimeTableResponse?>()

    @SuppressLint("NullSafeMutableLiveData")
    fun postLogin(email: String, password: String) {
        val loginModel = DataLoginModel(email, password)
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.postLogin(loginModel)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataLogin.postValue(myResponse)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun postEmail(token: String, email: String) {
        val emailModel = DataEmailModel(email)
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.postEmail(token, emailModel)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataUserInfo.postValue(myResponse)
            } else {
                liveDataUserInfoError.postValue("Error el post email no va")
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun postCheckIn(
        token: String,
        fecha: String,
        checkIn: Boolean,
        id: Int,
        name: String,
        lastname: String
    ) {
        val user = User(id, name, lastname)
        val checkInModel = DataCheckInModel(fecha, checkIn, user)
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.postCheckIn(token, checkInModel)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataCheckIn.postValue(myResponse)
            } else {
                liveDataCheckInError.postValue("Error el checkin no va")
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getCheckIn(
        token: String,
        id: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCheckIn(token, id)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataCheckInList.postValue(myResponse)
            }
        }
    }

    /*fun getTimeTable(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getTimeTable(token)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataTimeTable.postValue(myResponse)
            }
        }
    }*/

    class MyViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Context::class.java).newInstance(context)
        }
    }

}