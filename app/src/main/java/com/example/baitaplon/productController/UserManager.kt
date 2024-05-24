package com.example.baitaplon.productController

import com.example.baitaplon.data.User

object UserManager {
    private var currentUser: User? = null

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun setCurrentUser(user: User) {
        currentUser = user
    }

    fun clearCurrentUser() {
        currentUser = null
    }
}