package com.eretana.pokedex

import android.app.Application
import android.content.Intent
import com.eretana.pokedex.services.ServicePokedexUpdate

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //val serviceIntent = Intent(this, ServicePokedexUpdate::class.java)
        //startService(serviceIntent)
    }

}