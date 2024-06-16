package com.eretana.pokedex.tools

class StringTools {
    companion object{
        @JvmStatic
        fun toPokemonOrderFormat(order: Int): String {
            return String.format("#%04d", order)
        }
    }
}