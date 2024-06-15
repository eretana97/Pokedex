package com.eretana.pokedex.viewmodel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eretana.pokedex.api.RetrofitBuilder
import com.eretana.pokedex.entities.Pokemon
import com.eretana.pokedex.entities.PokemonResult
import com.eretana.pokedex.entities.PokemonResultItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VMHome : ViewModel(){

    private var pokemonList = MutableLiveData<List<Pokemon>>()
    private val limit : Int = 15
    private val offset : Int = 0

    fun getPokemons(){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitBuilder.api.getPokemons(limit = limit, offset = offset).enqueue(object :
                    Callback<PokemonResult> {
                    override fun onResponse(call: Call<PokemonResult>, response: Response<PokemonResult>) {
                        if(response.body() != null) {
                            val pokemonsItems : List<PokemonResultItem> = response.body()!!.results
                            fetchPokemonDetails(pokemonsItems)
                        }
                    }

                    override fun onFailure(p0: Call<PokemonResult>, p1: Throwable) {
                        Log.e("SERVICE_ERROR",p1.localizedMessage)
                    }
                })
            }catch (e: Exception){

            }
        }

    }

    fun fetchPokemonDetails(pokemonsItems : List<PokemonResultItem>){

        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            val detailedPokemonList = pokemonsItems.map { pokemon: PokemonResultItem ->
                val response = RetrofitBuilder.api.getPokemonByUrl(pokemon.url).execute()
                if(response.isSuccessful){
                    response.body()
                }else{
                    null
                }
            }.filterNotNull()


            pokemonList.postValue(detailedPokemonList)

        }

    }



    fun observerPokemonRows() : LiveData<List<Pokemon>>{
        return pokemonList;
    }



}
