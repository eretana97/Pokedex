package com.eretana.pokedex.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class Home : AppCompatActivity() {
    private lateinit var vmhome: ViewModel
    private lateinit var adapter: PokedexAdapter
    private lateinit var rcv : RecyclerView
    private lateinit var dao : DAOPokemon
    private lateinit var service : Intent
    private lateinit var searchview : SearchView
    private lateinit var searchbar : SearchBar
    private lateinit var btn_clear : MaterialButton

    private var isSearching : Boolean = false
    private var isSubmit: Boolean = false
    private val searchResults = mutableListOf<Pokemon>()
    private var pokemons = mutableListOf<Pokemon>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        //Iniciamos variables y objetos
        vmhome = VMHome(this)
        rcv = findViewById(R.id.rcv_pokedex)
        adapter = PokedexAdapter(mutableListOf())
        dao = RoomDB.getDatabase(this).pokemonDAO()
        service = Intent(this, ServicePokedexUpdate::class.java)
        searchview = findViewById(R.id.search_view)
        searchbar = findViewById(R.id.search_bar)
        btn_clear = findViewById(R.id.btn_clear)


        //Eliminar los registros para iniciar de 0
        deletePokemonsBackup()

        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter


        searchview.editText.setOnEditorActionListener { v, actionId, event ->
            if(!isSubmit){
                (vmhome as VMHome).getPokemonByName(v!!.text.toString())
                isSearching = true
                isSubmit = true
                searchview.hide()
                searchview.postDelayed({isSubmit = false},1000)
            }
            true
        }

        btn_clear.setOnClickListener {
            searchResults.clear()
            isSearching = false
            adapter.updateItems(pokemons)
        }



        (vmhome as VMHome).observerPokemonFromApi().observe(this) { results ->
            adapter.updateItems(results)
            saveOnLocalDB(results)
            startBackgroundService()
        }

        (vmhome as VMHome).localPokemonList.observe(this) { results ->
            if (!isSearching) {
                pokemons.clear()
                pokemons.addAll(results)
                adapter.updateItems(results)
            }
        }

        (vmhome as VMHome).observerPokemonByName().observe(this) { pokemon ->
            searchResults.clear()
            searchResults.add(pokemon)
            adapter.updateItems(searchResults)
        }


        (vmhome as VMHome).error.observe(this) { e ->
            if (e.isNotEmpty()) showErrorAlert(e)
        }

        //Traemos los primeros 15 pokemons
        (vmhome as VMHome).getPokemons()

    }


    private fun showErrorAlert(text: String){
        AlertDialog.Builder(this)
            .setTitle(R.string.error_alert_title)
            .setMessage(text)
            .setPositiveButton(R.string.ok,null)
            .show()
    }

    private fun deletePokemonsBackup(){
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllPokemons()
        }
    }

    private fun saveOnLocalDB(pokemons: List<Pokemon>){
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertAll(pokemons)
        }
    }

    private fun startBackgroundService(){
        CoroutineScope(Dispatchers.IO).launch {
            delay(30000L)
            startService(service)
        }
    }

}
