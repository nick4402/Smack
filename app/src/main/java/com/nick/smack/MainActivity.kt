package com.nick.smack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.nick.smack.databinding.ActivityMainBinding
import com.nick.smack.services.AuthService
import com.nick.smack.services.UserDataService
import com.nick.smack.utility.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        /*
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        */

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE))
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLogin) {
                userNameHeader.text = UserDataService.name
                userEmailHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName,"drawable",packageName)
                userImageHeader.setImageResource(resourceId)
                userImageHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginBtnHeader.text = "Logout"
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun loginBtnHeaderClicked(view : View) {
        if (AuthService.isLogin) {
            UserDataService.logout()
            userNameHeader.text = "Login"
            userEmailHeader.text = ""
            userImageHeader.setImageResource(R.drawable.profiledefault)
            userImageHeader.setBackgroundColor(Color.TRANSPARENT)
            loginBtnHeader.text = "Login"
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

     fun addChannelBtnHeaderClicked(view : View) {

    }


}