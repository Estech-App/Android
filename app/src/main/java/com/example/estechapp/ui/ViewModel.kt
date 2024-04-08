package com.example.estechapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.estechapp.data.Repository
import com.example.estechapp.data.models.DataLoginModel
import com.example.estechapp.data.models.DataLoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*class ViewModel(val context: Context): ViewModel() {

    private val repository = Repository(context)

    val liveDataLogin = MutableLiveData<DataLoginResponse>()

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

    class MyViewModelFactory(private val context: Context): ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Context::class.java).newInstance(context)
        }
    }

}*/