package com.nick.smack.services

import com.nick.smack.model.RegisterRequest
import com.nick.smack.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface RetrofitService {
    @POST("account/register")
    suspend fun register(@HeaderMap headers: HashMap<String, String>,
                      @Body request: RegisterRequest
    ): HashMap<String,String>
}