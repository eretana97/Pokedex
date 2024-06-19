package com.eretana.pokedex.tools

import android.util.Log

class PokeTools {
    companion object{
        @JvmStatic
        fun toPokemonOrderFormat(order: Int): String {
            return String.format("#%04d", order)
        }

        @JvmStatic
        fun statPercentage(baseStat : Int) : Int {
            val pc = (baseStat.toDouble() / 230) * 100
            Log.d("PokeTools","%" + pc)
            return pc.toInt()
        }
    }

}