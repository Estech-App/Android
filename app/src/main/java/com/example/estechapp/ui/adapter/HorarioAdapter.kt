package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.databinding.ItemHorarioBinding
import com.example.estechapp.data.models.Horario

class HorarioAdapter(private val horario: List<Horario>) : RecyclerView.Adapter<HorarioAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHorarioBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(horario: Horario){
            with(binding){
                horaHorario.text = horario.hora
                nombreAsignatura.text = horario.asignatura
                cursoHorario.text = horario.grupo
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHorarioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = horario.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clase = horario[position]
        /*if (clase){
            if (position % 2 == 0){
                val gris = ContextCompat.getColor(holder.itemView.context, R.color.gris)
                holder.binding.bg.setBackgroundColor(gris)
            } else {
                val blanco = ContextCompat.getColor(holder.itemView.context, R.color.white)
                holder.binding.bg.setBackgroundColor(blanco)
            }
        }*/

    }
}