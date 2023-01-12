package com.mobile.storyapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field: SerializedName("error")
    val error: Boolean,

    @field: SerializedName("message")
    val message: String,

    @field: SerializedName("loginResult")
    val userModel: UserModel
)

data class UserModel(
    @field: SerializedName("userId")
    val userId: String,

    @field: SerializedName("name")
    val name: String,

    @field: SerializedName("token")
    val token: String,
)