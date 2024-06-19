package com.eretana.pokedex.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.eretana.pokedex.R

class Splash : ComponentActivity() {

    private var justOpen = true
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        if (checkPermission()) {
            requestPostNotificationsPermission()
        }else {
            gotoHome()
        }

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission() : Boolean{
        return checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPostNotificationsPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun gotoHome(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        },3000);
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        if(checkPermission()){
            requestPostNotificationsPermission()
        }else if (!justOpen){
            gotoHome()
        }
    }

    override fun onPause() {
        super.onPause()
        justOpen = false
    }
}
