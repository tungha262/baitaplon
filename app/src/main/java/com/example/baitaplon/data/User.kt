package com.example.baitaplon.data

import dagger.hilt.android.lifecycle.HiltViewModel

data class User(
    val firstName : String,
    val lastName : String,
    val email : String
){
    constructor():this("", "", "")
}






