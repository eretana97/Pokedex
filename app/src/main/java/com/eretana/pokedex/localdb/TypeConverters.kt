package com.eretana.pokedex.localdb

import androidx.room.TypeConverter
import com.eretana.pokedex.entities.PokemonSprite
import com.eretana.pokedex.entities.PokemonStat
import com.eretana.pokedex.entities.PokemonTypeSlot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverters {

    @TypeConverter
    fun fromPokemonSprite(pokemonSprite: PokemonSprite): String {
        return Gson().toJson(pokemonSprite)
    }

    @TypeConverter
    fun toPokemonSprite(data: String): PokemonSprite {
        val type = object : TypeToken<PokemonSprite>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun fromPokemonTypeSlotList(types: List<PokemonTypeSlot>): String {
        return Gson().toJson(types)
    }

    @TypeConverter
    fun toPokemonTypeSlotList(data: String): List<PokemonTypeSlot> {
        val type = object : TypeToken<List<PokemonTypeSlot>>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun fromPokemonStatList(stats: List<PokemonStat>): String {
        return Gson().toJson(stats)
    }

    @TypeConverter
    fun toPokemonStatList(data: String): List<PokemonStat> {
        val type = object : TypeToken<List<PokemonStat>>() {}.type
        return Gson().fromJson(data, type)
    }

}