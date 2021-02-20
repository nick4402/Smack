package com.nick.smack.services

object AppConfig {
    val baseUrl = "https://nick4402.herokuapp.com/v1/"
    val headers: HashMap<String, String>
        get() {
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/json"
            return headers
        }


}