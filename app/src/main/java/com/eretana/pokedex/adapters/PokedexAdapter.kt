package com.eretana.pokedex.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eretana.pokedex.R
import com.eretana.pokedex.entities.Pokemon
import com.eretana.pokedex.tools.PokeTools
import com.google.android.material.chip.Chip
import com.google.android.material.progressindicator.LinearProgressIndicator

class PokedexAdapter(private val items: MutableList<Pokemon?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val VIEW_ITEM : Int = 0
    private val View_LOADING : Int = 1

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val pokemon_sprite : ImageView = itemView.findViewById(R.id.pokemon_sprite)
        val pokemon_name : TextView = itemView.findViewById(R.id.pokemon_name)
        val pokemon_order : TextView = itemView.findViewById(R.id.pokemon_order)
        val pokemon_line : LinearLayout = itemView.findViewById(R.id.pokemon_line)
        val pokemon_stat_attack : LinearProgressIndicator = itemView.findViewById(R.id.progress_stat_attack)
        val pokemon_stat_defense : LinearProgressIndicator = itemView.findViewById(R.id.progress_stat_defense)
        val pokemon_stat_speed : LinearProgressIndicator = itemView.findViewById(R.id.progress_stat_speed)

        val pokemon_chip_1 : Chip = itemView.findViewById(R.id.chip_type_1)
        val pokemon_chip_2 : Chip = itemView.findViewById(R.id.chip_type_2)
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
            itemholder.pokemon_order.text = PokeTools.toPokemonOrderFormat(pokemon.id)

            itemholder.pokemon_name.setTextColor(Color.parseColor(pokemon.types[0].type.getTypeColor()))

            Glide.with(itemholder.itemView.context).load(pokemon.sprites.other.home.front_default).into(itemholder.pokemon_sprite)

            itemholder.pokemon_line.setBackgroundColor(Color.parseColor(pokemon.types[0].type.getTypeColorSoft()))

            itemholder.pokemon_stat_attack.setProgress(PokeTools.statPercentage(pokemon.stats[1].base_stat),true)
            itemholder.pokemon_stat_defense.setProgress(PokeTools.statPercentage(pokemon.stats[2].base_stat),true)
            itemholder.pokemon_stat_speed.setProgress(PokeTools.statPercentage(pokemon.stats[5].base_stat),true)


            itemholder.pokemon_chip_1.text = holder.itemView.context.getString(pokemon.types[0].type.getTypeName())
            itemholder.pokemon_chip_1.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(pokemon.types[0].type.getTypeColor()))

            if(pokemon.types.size > 1){
                itemholder.pokemon_chip_2.text = holder.itemView.context.getString(pokemon.types[1].type.getTypeName())
                itemholder.pokemon_chip_2.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(pokemon.types[1].type.getTypeColor()))
                itemholder.pokemon_chip_2.visibility = View.VISIBLE
            }else{
                itemholder.pokemon_chip_2.visibility = View.INVISIBLE
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
        items.clear()
        items.addAll(news)
        items.add(null)

        notifyDataSetChanged()
    }

}