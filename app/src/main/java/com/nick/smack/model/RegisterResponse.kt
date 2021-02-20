package com.nick.smack.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("message") val responseMessage: ResponseMessage
)
