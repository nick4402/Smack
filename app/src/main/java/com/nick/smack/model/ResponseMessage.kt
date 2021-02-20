package com.nick.smack.model

import com.google.gson.annotations.SerializedName

data class ResponseMessage (
    @SerializedName("name") val name: String,
    @SerializedName("message") val message:String
)