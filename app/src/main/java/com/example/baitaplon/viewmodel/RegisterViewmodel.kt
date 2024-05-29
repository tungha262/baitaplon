package com.example.baitaplon.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.baitaplon.data.User
import com.example.baitaplon.util.RegisterFieldsState
import com.example.baitaplon.util.RegisterValidation
import com.example.baitaplon.util.Resource
import com.example.baitaplon.util.validateEmail
import com.example.baitaplon.util.validateFirstName
import com.example.baitaplon.util.validateLastName
import com.example.baitaplon.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RegisterViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel(){

    private val _register  = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register:Flow<Resource<FirebaseUser>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user:User, password: String){
        if(checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        _register.value = Resource.Success(it)
                        registerAccount(firstName = user.firstName, lastName = user.lastName,user.email, password)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }
        else{
            val registerFieldsState= RegisterFieldsState(
                validateEmail(user.email),validatePassword(password), validateFirstName(user.firstName), validateLastName(user.lastName)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }

        }
    }

    private fun checkValidation(user: User, password: String) : Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val firstNamevalidation = validateFirstName(user.firstName)
        val lastNamevalidation = validateFirstName(user.lastName)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
                    && firstNamevalidation is RegisterValidation.Success && lastNamevalidation is RegisterValidation.Success
        return shouldRegister
    }
    private fun registerAccount(firstName: String, lastName: String,email: String, password: String) {
        val client = OkHttpClient()
        val jsonObject = JsonObject()
        jsonObject.addProperty("firstName", firstName)
        jsonObject.addProperty("lastName", lastName)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("password", password)
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://serveruddd.onrender.com/api/insertaccount")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RegisterActivity", "Failed to register account", e)
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("RegisterActivity", "Account registered successfully")
                } else {
                    Log.e("RegisterActivity", "Failed to register account")
                }
            }
        })
    }


}