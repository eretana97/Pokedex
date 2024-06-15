package com.eretana.pokedex.entities

import com.google.gson.annotations.SerializedName

data class PokemonResult(
    @SerializedName("count") val count : Int,
    @SerializedName("results") val results : List<PokemonResultItem>
)
