package com.eretana.pokedex.entities

import com.google.gson.annotations.SerializedName

data class PokemonResultItem(
    @SerializedName("name") val name : String,
    @SerializedName("url") val url : String
)
