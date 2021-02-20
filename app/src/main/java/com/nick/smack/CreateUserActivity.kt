package com.nick.smack

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nick.smack.model.RegisterResponse
import com.nick.smack.services.AuthService
import com.nick.smack.services.Repository
import com.nick.smack.utility.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_creat_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userImage = "profileDefault"
    var userColor = "[0.5,0.5,0.5,1]"
    val repo = Repository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creat_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun createUserImageClicked(view : View) {
        val random = Random()
        var color = random.nextInt(2)
        var image = random.nextInt(28)

        if (color == 0) {
            userImage = "light$image"
        } else {
            userImage = "dark$image"
        }

        val resourceId = resources.getIdentifier(userImage,"drawable",packageName)
        createUserImage.setImageResource(resourceId)
    }


    fun createUserColorBtnClicked(view : View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createUserImage.setBackgroundColor(Color.rgb(r,g,b))

        val saveR = r.toDouble()/255
        val saveG = g.toDouble()/255
        val saveB = b.toDouble()/255

        userColor = "[$saveR, $saveG, $saveB, 1]"
    }

    fun createUserBtnClicked(view : View) {
        enableSpinner(true)
        val userName = creatUserNameTxt.text.toString()
        val email = createEmailTxt.text.toString()
        val password = createPasswordTxt.text.toString()
        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(this, email, password) { registerComplete ->
                if (registerComplete) {
                    AuthService.loginUser(this, email, password) { loginComplete ->
                        if (loginComplete) {
                            AuthService.addUser(this, userName, email, userImage, userColor) { addUserComplete ->
                                if (addUserComplete) {
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
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
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this,"Please Input Value ",Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun errorToast() {
        Toast.makeText(this,"Someting went wrong, please try again.",Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable:Boolean) {
        if (enable) {
            createSpinner.visibility = View.VISIBLE
            createUserBtn.isEnabled = false
            createUserColorBtn.isEnabled = false
            createUserImage.isEnabled = false
        } else {
            createSpinner.visibility = View.INVISIBLE
            createUserBtn.isEnabled = true
            createUserColorBtn.isEnabled = true
            createUserImage.isEnabled = true
        }
    }
}