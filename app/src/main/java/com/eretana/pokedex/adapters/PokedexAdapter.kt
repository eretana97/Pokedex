package com.eretana.pokedex.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eretana.pokedex.R
import com.eretana.pokedex.entities.Pokemon
import com.eretana.pokedex.tools.StringTools

class PokedexAdapter(private val items: MutableList<Pokemon?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val VIEW_ITEM : Int = 0
    private val View_LOADING : Int = 1

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val pokemon_sprite : ImageView = itemView.findViewById(R.id.pokemon_sprite)
        val pokemon_name : TextView = itemView.findViewById(R.id.pokemon_name)
        val pokemon_order : TextView = itemView.findViewById(R.id.pokemon_order)
        val pokemon_card : RelativeLayout = itemView.findViewById(R.id.pokemon_card)
        val pokemon_type_slot1 : ImageView = itemView.findViewById(R.id.pokemon_type_slot1)
        val pokemon_type_slot2 : ImageView = itemView.findViewById(R.id.pokemon_type_slot2)
    }

    class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == VIEW_ITEM){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon,parent,false)
            return ItemViewHolder(view)
        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading,parent,false)
        return LoadingViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemon : Pokemon? = items[position]

        if(holder is ItemViewHolder){
            val itemholder : (ItemViewHolder) = holder
            itemholder.pokemon_name.text = pokemon!!.name.uppercase()
            itemholder.pokemon_order.text = StringTools.toPokemonOrderFormat(pokemon.order)

            itemholder.pokemon_name.setTextColor(Color.parseColor(pokemon.types[0].type.getTypeColor()))

            Glide.with(itemholder.itemView.context).load(pokemon.sprites.other.home.front_default).into(itemholder.pokemon_sprite)

            itemholder.pokemon_card.setBackgroundColor(android.graphics.Color.parseColor(pokemon.types[0].type.getTypeColor()))

            Glide.with(itemholder.itemView.context).load(pokemon.types[0].type.getTypeSprite()).into(itemholder.pokemon_type_slot1)
            if(pokemon.types.size > 1){
                Glide.with((itemholder.itemView.context)).load(pokemon.types[1].type.getTypeSprite()).into(itemholder.pokemon_type_slot2)
            }else{
                Glide.with((itemholder.itemView.context)).load("").into(holder.pokemon_type_slot2)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) View_LOADING else VIEW_ITEM
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(news : List<Pokemon>)
    {

        Log.d("ADAPTER", "AGREGANDO")
        items.clear()
        news.forEach {
            Log.d("ADAPTER",it.name + " ID: " + it.id)
        }
        items.addAll(news)
        items.add(null)

        notifyDataSetChanged()
    }
}