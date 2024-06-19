package com.eretana.pokedex.entities

import com.eretana.pokedex.R
import com.google.gson.annotations.SerializedName

data class PokemonTypeSlot(
    @SerializedName("slot") val slot : Int,
    @SerializedName("type") val type: PokemonType
)

data class PokemonType(val name: String){

    fun getTypeName(): Int {
        if (name.equals("bug")){ return R.string.type_bug }
        if (name.equals("dark")){ return R.string.type_dark}
        if (name.equals("dragon")){ return R.string.type_dragon}
        if (name.equals("electric")){ return R.string.type_electric}
        if (name.equals("fairy")){ return R.string.type_fairy}
        if (name.equals("fighting")){ return R.string.type_fighting}
        if (name.equals("fire")){ return R.string.type_fire}
        if (name.equals("flying")){ return R.string.type_flying}
        if (name.equals("ghost")){ return R.string.type_ghost}
        if (name.equals("grass")){ return R.string.type_grass}
        if (name.equals("ground")){ return R.string.type_ground}
        if (name.equals("ice")){ return R.string.type_ice}
        if (name.equals("normal")){ return R.string.type_normal}
        if (name.equals("poison")){ return R.string.type_poison}
        if (name.equals("psychic")){ return R.string.type_psychic}
        if (name.equals("rock")){ return R.string.type_rock}
        if (name.equals("steel")){ return R.string.type_steel}
        if (name.equals("water")){ return R.string.type_water}
        return R.string.type_normal;
    }

    fun getTypeColor(): String{
        if (name.equals("bug")){ return "#90c02c"}
        if (name.equals("dark")){ return "#5a5366"}
        if (name.equals("dragon")){ return "#0a6dc4"}
        if (name.equals("electric")){ return "#f4d23b"}
        if (name.equals("fairy")){ return "#ec8fe6"}
        if (name.equals("fighting")){ return "#ce4069"}
        if (name.equals("fire")){ return "#ff9c54"}
        if (name.equals("flying")){ return "#8fa8dd"}
        if (name.equals("ghost")){ return "#5269ac"}
        if (name.equals("grass")){ return "#64bc5e"}
        if (name.equals("ground")){ return "#ff9c54"}
        if (name.equals("ice")){ return "#73cebf"}
        if (name.equals("normal")){ return "#8e979f"}
        if (name.equals("poison")){ return "#ab6ac8"}
        if (name.equals("psychic")){ return "#f97176"}
        if (name.equals("rock")){ return "#c9b68b"}
        if (name.equals("steel")){ return "#5b8ea1"}
        if (name.equals("water")){ return "#4d90d5"}
        return "NaN";
    }

    fun getTypeColorSoft(): String{
        if (name.equals("bug")){ return "#9090c02c"}
        if (name.equals("dark")){ return "#905a5366"}
        if (name.equals("dragon")){ return "#900a6dc4"}
        if (name.equals("electric")){ return "#90f4d23b"}
        if (name.equals("fairy")){ return "#90ec8fe6"}
        if (name.equals("fighting")){ return "#90ce4069"}
        if (name.equals("fire")){ return "#90ff9c54"}
        if (name.equals("flying")){ return "#908fa8dd"}
        if (name.equals("ghost")){ return "#905269ac"}
        if (name.equals("grass")){ return "#9064bc5e"}
        if (name.equals("ground")){ return "#90ff9c54"}
        if (name.equals("ice")){ return "#9073cebf"}
        if (name.equals("normal")){ return "#908e979f"}
        if (name.equals("poison")){ return "#90ab6ac8"}
        if (name.equals("psychic")){ return "#90f97176"}
        if (name.equals("rock")){ return "#90c9b68b"}
        if (name.equals("steel")){ return "#905b8ea1"}
        if (name.equals("water")){ return "#904d90d5"}
        return "NaN";
    }

}