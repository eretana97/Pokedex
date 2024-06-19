package com.eretana.pokedex.viewmodel
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.eretana.pokedex.R
import com.eretana.pokedex.api.RetrofitBuilder
import com.eretana.pokedex.daos.DAOPokemon
import com.eretana.pokedex.entities.Pokemon
import com.eretana.pokedex.entities.PokemonResult
import com.eretana.pokedex.entities.PokemonResultItem
import com.eretana.pokedex.localdb.RoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VMHome(context : Context) : ViewModel(){

    private var pokemonList = MutableLiveData<List<Pokemon>>()
    private var pokemon = MutableLiveData<Pokemon>()
    private val dao : DAOPokemon = RoomDB.getDatabase(context).pokemonDAO()

    var localPokemonList = dao.getAllPokemons().asLiveData()
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val NETWORK_ERROR : String = context.getString(R.string.network_error)
    val NOT_FOUND : String = context.getString(R.string.pokemon_not_found)
    val NaN : String = ""


    fun getPokemons(){

        val limit = 15
        val offset = 0
        _error.value = NaN

        RetrofitBuilder.api.getPokemons(limit = limit, offset = offset).enqueue(object : Callback<PokemonResult> {
            override fun onResponse(call: Call<PokemonResult>, response: Response<PokemonResult>) {
                if(response.body() != null) {
                    val pokemonsItems : List<PokemonResultItem> = response.body()!!.results
                    fetchPokemonDetails(pokemonsItems)
                    _error.value = NaN
                }
            }

            override fun onFailure(p0: Call<PokemonResult>, p1: Throwable) {
               _error.value = NETWORK_ERROR
            }
        })

    }

    fun getPokemonByName(name : String){
        Log.d("VMHOME","GET POKEMON BY NAME")
        RetrofitBuilder.api.getPokemonByName(name).enqueue(object : Callback<Pokemon>{
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if(response.body() != null){
                    pokemon.value = response.body()
                    _error.value = NaN
                }else{
                    _error.value = NOT_FOUND
                }
            }

            override fun onFailure(p0: Call<Pokemon>, p1: Throwable) {
                _error.value = NETWORK_ERROR
            }
        })
    }



    fun fetchPokemonDetails(pokemonsItems : List<PokemonResultItem>){

        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            val detailedPokemonList = pokemonsItems.map { pokemon: PokemonResultItem ->
                val response = RetrofitBuilder.api.getPokemonByUrl(pokemon.url).execute()
                if(response.isSuccessful){
                    _error.postValue(NaN)
                    response.body()

                }else{
                    _error.value = NETWORK_ERROR
                    null
                }
            }.filterNotNull()

            pokemonList.postValue(detailedPokemonList)

        }

    }



    fun observerPokemonFromApi() : LiveData<List<Pokemon>>{
        return pokemonList;
    }


    fun observerPokemonByName() : LiveData<Pokemon>{
        return pokemon
    }



}
