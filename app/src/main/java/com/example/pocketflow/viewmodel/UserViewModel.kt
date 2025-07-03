package com.example.pocketflow.viewmodel

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pocketflow.model.AppDatabase
import com.example.pocketflow.model.SharedPref
import com.example.pocketflow.model.User
import com.example.pocketflow.view.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val sharedPref = SharedPref(application)
    private val daoUser = AppDatabase.getDatabase(application).userDao()

    val signupResult = MutableLiveData<String>()
    val signinResult = MutableLiveData<String?>()

    val loggedInUserId = MutableLiveData<Int?>()
    val selectedUser = MutableLiveData<User>()

    //buat databinding
    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()

    val resultChangePass = MutableLiveData<String>()


    fun registerUser(user: User) {
        launch() {
            val existingUser = daoUser.checkUsername(user.username)
            if (existingUser != null) {
                signupResult.postValue("Username Already Used")
            } else {
                daoUser.insert(user)
                signupResult.postValue("Sign Up Succesfully")
            }
        }
    }
    fun login(username: String, password: String) {
        launch() {
            val user = daoUser.login(username, password)
            if (user != null) {
                loggedInUserId.postValue(user.id)
                signinResult.postValue("Login Succesfully")
            } else {
                signinResult.postValue("Wrong Username or Password")
            }
        }
    }

    fun clearLoginResult() {
        signinResult.value = null
    }

    //ambil data user
    fun getUserData(id:Int){
        launch {
            val db = AppDatabase.getDatabase(getApplication())
            selectedUser.postValue(db.userDao().getUserById(id))
        }
    }

    //databinding
    fun changePass() {
        launch {
            val userId = sharedPref.getUserId()
            val user = daoUser.getUserById(userId)

            if (oldPassword.value.isNullOrBlank() || newPassword.value.isNullOrBlank() || repeatPassword.value.isNullOrBlank()) {
                popUpMessage("Please fill all fields")
                return@launch
            }

            if (oldPassword.value != user.password) {
                popUpMessage("Old password is incorrect")
                return@launch
            }

            if (newPassword.value != repeatPassword.value) {
                popUpMessage("New passwords do not match")
                return@launch
            }

            daoUser.changePassword(newPassword.value.toString(), user.id)
                resultChangePass.postValue("Password updated successfully") // gunakan postValue
            popUpMessage("Password updated successfully")
            clearText()
        }
    }

    //buat munculin pesan
    private fun popUpMessage(message: String) {
        launch{
            withContext(Dispatchers.Main) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //ngosongin semua text
    private fun clearText() {
        oldPassword.postValue("")
        newPassword.postValue("")
        repeatPassword.postValue("")
    }

    //buat Logout
    fun logOut() {
        sharedPref.logout()
        val context = getApplication<Application>().applicationContext
        val intent = Intent(context, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)
    }


}