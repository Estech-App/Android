package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.R
import com.example.estechapp.databinding.ItemTutoriasHotBinding
import com.example.estechapp.data.models.Tutoria

class TutoriasHotAdapter(private val tutorias: List<Tutoria>) : RecyclerView.Adapter<TutoriasHotAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTutoriasHotBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(tutoria: Tutoria){
            binding.nombre.text = tutoria.alumno
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding  = ItemTutoriasHotBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return  ViewHolder(binding)
    }

    override fun getItemCount() = tutorias.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tutorias[position])
        if (position % 2 == 0){
            val gris = ContextCompat.getColor(holder.itemView.context, R.color.gris)
            holder.binding.bg.setBackgroundColor(gris)
        } else {
            val blanco = ContextCompat.getColor(holder.itemView.context, R.color.white)
            holder.binding.bg.setBackgroundColor(blanco)
        }
    }

}