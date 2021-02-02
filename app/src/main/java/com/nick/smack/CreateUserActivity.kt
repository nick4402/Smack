package com.nick.smack

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_creat_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userImage = "profileDefault"
    var userColor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creat_user)
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
    }

    fun createUserBtnClicked(view : View) {

    }
}