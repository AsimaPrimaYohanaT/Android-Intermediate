package com.example.mystoryapp

import com.example.mystoryapp.data.UserModel

object UserModelDummy {

    fun generateDummyUserModel(): UserModel {
        return UserModel(
            "asimaprima",
            "userId",
            "token",
            true,
        )
    }
}