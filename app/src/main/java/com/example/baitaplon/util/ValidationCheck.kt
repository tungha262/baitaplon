package com.example.baitaplon.util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    if(email.isEmpty())
        return RegisterValidation.Failed("Email không thể để trống")
    if(!email.endsWith("@gmail.com"))
        return RegisterValidation.Failed("Email không hợp lệ")
    return RegisterValidation.Success
}
fun validatePassword(password: String): RegisterValidation {
    if(password.isEmpty())
        return RegisterValidation.Failed("Mật khẩu không thể để trống")
    if(password.length < 6)
        return RegisterValidation.Failed("Mật khẩu quá ngắn")
    return RegisterValidation.Success
}
fun validateFirstName(firstName: String): RegisterValidation {
    if(firstName.isEmpty())
        return RegisterValidation.Failed("Tên không thể để trống")
    return RegisterValidation.Success
}
fun validateLastName(lastName: String): RegisterValidation {
    if(lastName.isEmpty())
        return RegisterValidation.Failed("Họ không thể để trống")
    return RegisterValidation.Success
}

