package com.eretana.pokedex.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.eretana.pokedex.R
import com.eretana.pokedex.api.RetrofitBuilder
import com.eretana.pokedex.localdb.RoomDB
import com.eretana.pokedex.entities.PokemonResultItem
import com.eretana.pokedex.entities.PokemonResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServicePokedexUpdate : Service() {


    private lateinit var context: Context
    private lateinit var handler : Handler
    private lateinit var appDatabase: RoomDB
    private var limit : Int = 10
    private var offset: Int = 15
    private val CHANEL_ID = "0"
    private val NOTIFICATION_ID = 19970507
    private val TIMER : Long = 30*1000


    private val fetchRunnable = object : Runnable{
        override fun run() {
            fetchPokemonList()
            handler.postDelayed(this,TIMER)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        appDatabase = RoomDB.getDatabase(context)

        handler = Handler(Looper.getMainLooper())
        handler.post(fetchRunnable)

        createNotificationChannel()

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(fetchRunnable)

    }

    private fun fetchPokemonList(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitBuilder.api.getPokemons(limit = limit, offset = offset).enqueue(object : Callback<PokemonResult> {
                    override fun onResponse(call: Call<PokemonResult>, response: Response<PokemonResult>) {
                        if(response.body() != null) {
                            val pokemonsItems : List<PokemonResultItem> = response.body()!!.results
                            fetchPokemonDetails(pokemonsItems)
                            Log.d("SERVICE","POKEMON SIZE: " + pokemonsItems.size + " FIRST: " + pokemonsItems.first().name + " LAST: " + pokemonsItems.last().name)
                        }
                        offset += 10

                    }

                    override fun onFailure(p0: Call<PokemonResult>, p1: Throwable) {
                        showDownloadFailureNotification()
                    }
                })
            }catch (e: Exception){
                showDownloadFailureNotification()
            }
        }
    }

    private fun fetchPokemonDetails(items: List<PokemonResultItem>){

        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            val detailedPokemonList = items.map { pokemon: PokemonResultItem ->
                val response = RetrofitBuilder.api.getPokemonByUrl(pokemon.url).execute()
                if(response.isSuccessful){
                    response.body()
                }else{
                    showDownloadFailureNotification()
                    null
                }
            }.filterNotNull()

            appDatabase.pokemonDAO().insertAll(detailedPokemonList)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if(checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                    showDownloadSuccessNotification()
                }else{
                    requestPostNotificationsPermission()
                }
            }else{
                showDownloadSuccessNotification()
            }

        }

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val name = context.getString(R.string.notification_channel_name)
            var description = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANEL_ID, name, importance).apply {
                description = description
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showDownloadSuccessNotification(){
        val builder = NotificationCompat.Builder(this,CHANEL_ID)
            .setSmallIcon(R.drawable.pokeball_icon)
            .setContentTitle(context.getString(R.string.download_success))
            .setContentText(context.getString(R.string.notification_success_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)){
            notify(NOTIFICATION_ID,builder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun showDownloadFailureNotification(){
        val builder = NotificationCompat.Builder(this,CHANEL_ID)
            .setSmallIcon(R.drawable.ic_network_error)
            .setContentTitle(context.getString(R.string.network_error))
            .setContentText(context.getString(R.string.dowload_error))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)){
            notify(NOTIFICATION_ID,builder.build())
        }
    }


    private fun requestPostNotificationsPermission() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}