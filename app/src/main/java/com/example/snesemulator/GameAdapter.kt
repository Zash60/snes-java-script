package com.example.snesemulator

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Rom(val uri: Uri, val name: String)

class GameAdapter(private val onRomClick: (Uri) -> Unit) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private val roms = mutableListOf<Rom>()

    fun submitList(newRoms: List<Rom>) {
        roms.clear()
        roms.addAll(newRoms)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rom = roms[position]
        holder.bind(rom)
        holder.itemView.setOnClickListener { onRomClick(rom.uri) }
    }

    override fun getItemCount(): Int = roms.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameName: TextView = itemView.findViewById(R.id.game_name)

        fun bind(rom: Rom) {
            gameName.text = rom.name
        }
    }
}