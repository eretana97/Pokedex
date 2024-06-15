package com.eretana.pokedex.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eretana.pokedex.entities.Pokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface DAOPokemon {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<Pokemon>)

    @Query("Select * from pokemons order by id asc")
    suspend fun getAllPokemons(): List<Pokemon>

    @Query("DELETE FROM pokemons")
    suspend fun deleteAllPokemons()

}

