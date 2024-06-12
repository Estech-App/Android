package com.example.estechapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.estechapp.data.Repository
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
import com.example.estechapp.data.models.DataRoleResponse
import com.example.estechapp.data.models.DataRoomResponse
import com.example.estechapp.data.models.DataUserInfoResponse
import com.example.estechapp.data.models.Grupos
import com.example.estechapp.data.models.RoomId
import com.example.estechapp.data.models.User
import com.example.estechapp.data.models.UserFull
import com.example.estechapp.data.models.UserFullVerdat
import com.example.estechapp.data.models.UserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(val context: Context) : ViewModel() {

    private val repository = Repository(context)

    //Aqui todos los liveData, el SingleLiveEvent lo explico en su clase.
    val liveDataLogin = MutableLiveData<DataLoginResponse>()
    val liveDataLoginError = MutableLiveData<String>()
    val liveDataUserInfo = MutableLiveData<DataUserInfoResponse>()
    val liveDataUserInfoError = MutableLiveData<String>()
    val liveDataCheckIn = SingleLiveEvent<DataCheckInResponse>()
    val liveDataCheckInError = SingleLiveEvent<String>()
    val liveDataCheckInList = MutableLiveData<List<DataCheckInResponse>>()
    val liveDataMentoringList = MutableLiveData<List<DataMentoringResponse>>()
    val liveDataMentoring = SingleLiveEvent<DataMentoringResponse>()
    val liveDataRoomList = MutableLiveData<List<DataRoomResponse>>()
    val liveDataRoom = MutableLiveData<DataRoomResponse>()
    val liveDataRoleList = MutableLiveData<List<DataRoleResponse>>()
    val liveDataUserRoleList = MutableLiveData<List<UserFull>>()
    //val liveDataTimeTable = MutableLiveData<DataTimeTableResponse?>()
    val liveDataRoomAndMentoring = MediatorLiveData<Pair<List<DataRoomResponse>,List<DataMentoringResponse>>>()
    val liveDataFreeUsage = SingleLiveEvent<DataFreeUsageResponse>()
    val liveDataFreeUsageList = MutableLiveData<List<DataFreeUsageResponse>>()
    val liveDataRoomAndFreeUsage = MediatorLiveData<Pair<List<DataRoomResponse>,List<DataFreeUsageResponse>>>()
    val liveDataUserGroups = MutableLiveData<UserFullVerdat>()
    val liveDataGroupUser = MutableLiveData<List<Grupos>>()

    init {
        liveDataRoomAndMentoring.addSource(liveDataRoomList) { rooms ->
            liveDataRoomAndMentoring.value = Pair(rooms, liveDataMentoringList.value ?: emptyList())
        }
        liveDataRoomAndMentoring.addSource(liveDataMentoringList) { mentorings ->
            liveDataRoomAndMentoring.value = Pair(liveDataRoomList.value ?: emptyList(), mentorings)
        }
    }

    init {
        liveDataRoomAndFreeUsage.addSource(liveDataRoomList) { rooms ->
            liveDataRoomAndFreeUsage.value = Pair(rooms, liveDataFreeUsageList.value ?: emptyList())
        }
        liveDataRoomAndFreeUsage.addSource(liveDataFreeUsageList) { freeUsage ->
            liveDataRoomAndFreeUsage.value = Pair(liveDataRoomList.value ?: emptyList(), freeUsage)
        }
    }

    //Todas las funciones son casi iguales
    //El postLogin para hacer el login y recibir el correo para luego pasarselo al postEmail.
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

    //El postEmail para pasarle el correo y recibir la informacion.
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

    //El postCheckIn para hacer los fichajes.
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

    //El getCheckIn para recibir todos los checkin por id.
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

    //El getMentoringTeacher para recibir todas las tutorias por id
    @SuppressLint("NullSafeMutableLiveData")
    fun getMentoringTeacher(
        token: String,
        id: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getMentoringTeacher(token, id)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataMentoringList.postValue(myResponse)
            }
        }
    }

    //Este hace lo mismo que el getMentoringTeacher pero se ha dividido en 2 nose porque
    //si en el backend son lo mismo.
    @SuppressLint("NullSafeMutableLiveData")
    fun getMentoringStudent(
        token: String,
        id: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getMentoringStudent(token, id)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataMentoringList.postValue(myResponse)
            }
        }
    }

    //El getRoomList para recibir todas las aulas.
    @SuppressLint("NullSafeMutableLiveData")
    fun getRoomList(
        token: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getRoomList(token)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataRoomList.postValue(myResponse)
            }
        }
    }

    //El getRoomId para recibir el nombre de la sala pasandole el roomId.
    @SuppressLint("NullSafeMutableLiveData")
    fun getRoomId(
        token: String,
        id: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getRoomById(token, id)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataRoom.postValue(myResponse)
            }
        }
    }

    fun getRoleList(
        token: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getAllRoles(token)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataRoleList.postValue(myResponse!!)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getUserByRole(
        token: String,
        role: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getUserByRole(token, role)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataUserRoleList.postValue(myResponse)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun postMentoring(
        token: String,
        start: String,
        end: String,
        roomId: Int?,
        status: String,
        teacherId: Int,
        studentId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val teacher = UserId(teacherId)
            val student = UserId(studentId)
            val mentoringModel = DataMentoringModel(start, end, roomId, status, teacher, student)
            val response = repository.postMentoring(token, mentoringModel)
            if(response.isSuccessful) {
                val myResponse = response.body()
                liveDataMentoring.postValue(myResponse)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun patchMentoring(
        token: String,
        id: Int,
        start: String?,
        end: String?,
        roomId: Int?,
        status: String?
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            val mentoringModel = DataMentoringModelPatch(start, end, roomId, status)
            val response = repository.patchMentoring(token, id, mentoringModel)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataMentoring.postValue(myResponse)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getFreeUsage(
        token: String,
        id: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getFreeUsage(token, id)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataFreeUsageList.postValue(myResponse)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun patchFreeUsage(
        token: String,
        id: Int,
        status: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val freeUsageModel = DataFreeUsageModelPatch(status)
            val response = repository.patchFreeUsage(token, id, freeUsageModel)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataFreeUsage.postValue(myResponse)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun postFreeUsage(
        token: String,
        start: String,
        end: String,
        status: String,
        roomId: Int,
        userId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val room = RoomId(roomId)
            val user = UserId(userId)
            val freeUsageModel = DataFreeUsageModel(start, end, status, room, user)
            val response = repository.postFreeUsage(token, freeUsageModel)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataFreeUsage.postValue(myResponse)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getUserStudent(
        token: String,
        id: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getUserStudent(token, id)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataUserGroups.postValue(myResponse)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getGroupUser(
        token: String,
        id: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getGroupUser(token, id)
            if (response.isSuccessful) {
                val myResponse = response.body()
                liveDataGroupUser.postValue(myResponse)
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