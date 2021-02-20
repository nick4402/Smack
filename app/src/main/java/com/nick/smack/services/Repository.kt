package com.nick.smack.services

import com.google.gson.GsonBuilder
import com.nick.smack.model.RegisterRequest
import com.nick.smack.model.RegisterResponse
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Repository() {

    fun createRetrofit() : RetrofitService{
        val retrofit = Retrofit.Builder()
            //client(okHttpClient)
                .baseUrl(AppConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(RetrofitService::class.java)
    }

    suspend fun register(email:String, password:String) : HashMap<String,String>{
        val retro = createRetrofit()
        println("email $email password $password")
        return retro.register(AppConfig.headers,RegisterRequest(email,password))

    }

}
