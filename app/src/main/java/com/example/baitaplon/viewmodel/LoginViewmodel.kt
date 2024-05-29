package com.example.baitaplon.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitaplon.data.User
import com.example.baitaplon.util.RegisterValidation
import com.example.baitaplon.util.Resource
import com.example.baitaplon.util.validateEmail
import com.example.baitaplon.util.validateFirstName
import com.example.baitaplon.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel (){
    private val lo_gin = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val login: Flow<Resource<FirebaseUser>> = lo_gin

    fun login (email:String, password:String){
        if(email.isEmpty() || password.isEmpty()){
            viewModelScope.launch {
                lo_gin.emit(Resource.Error("Email và mật khẩu không được để trống!"))
            }
        }
        else {
            viewModelScope.launch {
                lo_gin.emit(Resource.Loading())
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            lo_gin.emit(Resource.Success(it))
                        }
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        lo_gin.emit(Resource.Error("Email hoặc mật khẩu không chính xác!"))
                    }
                }
        }
    }
}