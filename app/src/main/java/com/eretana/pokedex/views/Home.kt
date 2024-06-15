package com.eretana.pokedex.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView.OnScrollListener
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.eretana.pokedex.R
import com.eretana.pokedex.viewmodel.VMHome
import com.eretana.pokedex.adapters.PokedexAdapter
import com.eretana.pokedex.daos.DAOPokemon
import com.eretana.pokedex.entities.Pokemon
import com.eretana.pokedex.localdb.RoomDB
import com.eretana.pokedex.services.ServicePokedexUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : ComponentActivity() {
    private lateinit var vmhome: ViewModel
    private lateinit var adapter: PokedexAdapter
    private lateinit var rcv : RecyclerView
    private lateinit var dao : DAOPokemon
    private lateinit var service : Intent

    private var isLoading : Boolean = false

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Iniciamos variables y objetos
        vmhome = VMHome()
        rcv = findViewById(R.id.rcv_pokedex)
        adapter = PokedexAdapter(mutableListOf())
        dao = RoomDB.getDatabase(this).pokemonDAO()
        service = Intent(this, ServicePokedexUpdate::class.java)


        //Eliminar los registros para iniciar de 0
        deletePokemonsBackup()
        //Iniciar el observador de scroll del rcv
        initScrollListener()


        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter




        (vmhome as VMHome).observerPokemonRows().observe(this, Observer { pokemons ->
            adapter.updateItems(pokemons)
            saveOnLocalDB(pokemons)
            startBackgroundService()
        })

        //Traemos los primeros 15 pokemons
        (vmhome as VMHome).getPokemons()


    }

    private fun initScrollListener(){

        rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d("SCROLL",newState.toString())

                val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.itemCount - 1) {
                        getNewPokemons()
                        isLoading = true
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })

    }

    private fun deletePokemonsBackup(){

        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllPokemons()
        }
    }

    private fun saveOnLocalDB(pokemons: List<Pokemon>){

        CoroutineScope(Dispatchers.IO).launch {
            dao.insertAll(pokemons)
            val count : Int = dao.getAllPokemons().size
            Log.d("HOME","POKEMON SAVED: " + count)
        }
    }

    private fun startBackgroundService(){
        CoroutineScope(Dispatchers.IO).launch {
            delay(30000L)
            startService(service)
        }
    }

    private fun getNewPokemons(){

        CoroutineScope(Dispatchers.IO).launch {
            val newpokemons = dao.getAllPokemons()
            withContext(Dispatchers.Main){
                adapter.updateItems(newpokemons)
                isLoading = false
            }
        }

    }

}
