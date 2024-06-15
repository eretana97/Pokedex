package com.eretana.pokedex.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pokemons")
data class Pokemon(
    @PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("name") val name : String,
    @SerializedName("order") val order: Int,
    @SerializedName("sprites") val sprites : PokemonSprite,
    @SerializedName("types") val types: List<PokemonTypeSlot>
)

data class PokemonSprite(
    @SerializedName("other") val other: PokemonSpriteOther
)

data class PokemonSpriteOther(
    @SerializedName("home") val home: PokemonSpriteHome
)

data class PokemonSpriteHome(
    @SerializedName("front_default") val front_default: String
)

