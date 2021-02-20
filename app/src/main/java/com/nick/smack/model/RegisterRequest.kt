package com.nick.smack.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email") var email: String,
    @SerializedName("password") val password: String
)