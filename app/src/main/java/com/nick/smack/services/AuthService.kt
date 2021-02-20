package com.nick.smack.services

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.HashMap
import com.android.volley.DefaultRetryPolicy

import android.R.string.no
import android.content.Intent
import android.service.autofill.UserData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nick.smack.utility.*
import org.json.JSONException


/**
 * Created by jonnyb on 9/1/17.
 */
object AuthService {
    var authToken = ""
    var isLogin = false
    var userEmail = ""

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            println("Success " + response)
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR6", "Could not register user: ${error.toString()}")
            complete(false)
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

        }

        registerRequest.setRetryPolicy(DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
            Log.d("Success", response.toString())
            try {
                userEmail = response.getString("user")
                authToken = response.getString("token")
                isLogin = true
                complete(true)
            } catch (e : JSONException) {
                Log.d("JSON","Exception "+e.localizedMessage)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR2", "Could not register user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        loginRequest.setRetryPolicy(DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun addUser(context: Context,name:String , email: String, avatarName: String,avatarColor :String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val addUserRequest = object : JsonObjectRequest(Method.POST, URL_ADD_USER, null, Response.Listener { response ->
            Log.d("Success", response.toString())
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                complete(true)
            } catch (e : JSONException) {
                Log.d("JSON","Exception "+e.localizedMessage)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not add user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers.put("Authorization","Bearer $authToken")
                return headers
            }
        }

        addUserRequest.setRetryPolicy(DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(addUserRequest)
    }

    fun findUserByEmail(context : Context,complete:(Boolean) -> Unit) {
        val getUserRequest = object : JsonObjectRequest(Method.GET, "$URL_GET_USER$userEmail", null, Response.Listener { response ->
            Log.d("Success", response.toString())
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")
                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            } catch (e : JSONException) {
                Log.d("JSON","Exception "+e.localizedMessage)
            }
        },Response.ErrorListener { error ->
            Log.d("Error","Clude not file user $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers.put("Authorization","Bearer $authToken")
                return headers
            }
        }

        getUserRequest.setRetryPolicy(DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        Volley.newRequestQueue(context).add(getUserRequest)
    }
}