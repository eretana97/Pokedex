package com.eretana.pokedex.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.eretana.pokedex.daos.DAOPokemon
import com.eretana.pokedex.entities.Pokemon

@Database(entities = [Pokemon::class], version = 1, exportSchema = false)
@TypeConverters(com.eretana.pokedex.localdb.TypeConverters::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun pokemonDAO() : DAOPokemon

    companion object {

        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDatabase(context: Context) : RoomDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,RoomDB::class.java,"pokemon_database").build()
                INSTANCE = instance
                instance
            }
        }

    }

}