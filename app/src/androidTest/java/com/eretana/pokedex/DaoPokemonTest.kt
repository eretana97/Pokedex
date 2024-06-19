package com.eretana.pokedex

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.eretana.pokedex.daos.DAOPokemon
import com.eretana.pokedex.entities.Pokemon
import com.eretana.pokedex.entities.PokemonSprite
import com.eretana.pokedex.localdb.RoomDB
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DaoPokemonTest {

    private lateinit var db: RoomDB
    private lateinit var dao : DAOPokemon

    @Before
    fun createDb(){

        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), RoomDB::class.java).allowMainThreadQueries().build()
        dao = db.pokemonDAO()
    }

    @After
    fun closeDB(){
        db.close()
    }

    @Test
    fun insertAndGetAllPokemons() = runBlocking {
        val gson = Gson()
        val pokemons = listOf(
            Pokemon(6, "Charizard", 6, gson.fromJson("""{"other":{"home":{"front_default":"url1"}}}""",PokemonSprite::class.java), listOf(), listOf()),
            Pokemon(25, "Pikachu", 25, gson.fromJson("""{"other":{"home":{"front_default":"url1"}}}""",PokemonSprite::class.java), listOf(), listOf()),
            Pokemon(1, "Bulbasaur", 1, gson.fromJson("""{"other":{"home":{"front_default":"url1"}}}""",PokemonSprite::class.java), listOf(), listOf()),
        )

        dao.insertAll(pokemons)
        val pokedex = dao.getAllPokemons()

        Assert.assertEquals(3,pokedex.size)
        Assert.assertEquals("Bulbasaur",pokedex[0].name)
        Assert.assertEquals("Charizard",pokedex[1].name)
        Assert.assertEquals("Pikachu",pokedex[2].name)

    }

    @Test
    fun deleteAllPokemons() = runBlocking {
        val gson = Gson()
        val pokemons = listOf(
            Pokemon(6, "Charizard", 6, gson.fromJson("""{"other":{"home":{"front_default":"url1"}}}""",PokemonSprite::class.java), listOf(), listOf()),
            Pokemon(25, "Pikachu", 25, gson.fromJson("""{"other":{"home":{"front_default":"url1"}}}""",PokemonSprite::class.java), listOf(), listOf()),
            Pokemon(1, "Bulbasaur", 1, gson.fromJson("""{"other":{"home":{"front_default":"url1"}}}""",PokemonSprite::class.java), listOf(), listOf()),
        )

        dao.insertAll(pokemons)
        dao.deleteAllPokemons()

        val pokedex = dao.getAllPokemons()
        Assert.assertTrue(pokedex.isEmpty())

    }


}