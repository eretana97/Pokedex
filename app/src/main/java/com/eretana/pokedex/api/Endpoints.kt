package com.eretana.pokedex.api

import com.eretana.pokedex.entities.Pokemon
import com.eretana.pokedex.entities.PokemonResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface Endpoints {

    @GET("pokemon")
    fun getPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<PokemonResult>;

    @GET("pokemon/{id}")
    fun getPokemon(
        @Path("id") id: Int
    ): Call<Pokemon>

    @GET
    fun getPokemonByUrl(@Url url: String) : Call<Pokemon>

}