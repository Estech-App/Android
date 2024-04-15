package com.example.estechapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.estechapp.data.Repository
import com.example.estechapp.data.models.DataEmailModel
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.models.DataLoginResponse
import com.example.estechapp.data.models.DataUserInfoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(val context: Context): ViewModel() {

    private val repository = Repository(context)

    val liveDataLogin = MutableLiveData<DataLoginResponse>()
    val liveDataLoginError = MutableLiveData<String>()
    val liveDataUserInfo = MutableLiveData<DataUserInfoResponse>()

    @SuppressLint("NullSafeMutableLiveData")
    fun postLogin(email: String, password: String) {
        val loginModel = DataLoginModel(email, password)
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.postLogin(loginModel)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataLogin.postValue(myResponse)
            } else {
                liveDataLoginError.postValue("El correo o la contraseña son incorrectos")
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
            }
        }
    }

    class MyViewModelFactory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Context::class.java).newInstance(context)
        }
    }

}