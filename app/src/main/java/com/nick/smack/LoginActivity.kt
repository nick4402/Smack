package com.nick.smack

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.nick.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_creat_user.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.createUserBtn

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginBtnClicked(view : View) {
        hideKeyboard()
        enableSpinner(true)
        val email = loginEmailTxt.text.toString()
        val pass = loginPasswordTxt.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            AuthService.loginUser(this, email, pass) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { getUserSuccess ->
                        if (getUserSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this,"Please input email and password",Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun createUserBtnClicked(view : View) {
        val createUserIntent = Intent(this,CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast() {
        Toast.makeText(this,"Someting went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable:Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
            createUserBtn.isEnabled = false
            loginBtn.isEnabled = false
        } else {
            loginSpinner.visibility = View.INVISIBLE
            createUserBtn.isEnabled = true
            loginBtn.isEnabled = true
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }
}