package com.eretana.pokedex.entities

import com.google.gson.annotations.SerializedName

class PokemonStat(
    @SerializedName("base_stat") val base_stat : Int = 0,
    @SerializedName("stat") val stat : StatName
)

class StatName(@SerializedName("name") val name : String)