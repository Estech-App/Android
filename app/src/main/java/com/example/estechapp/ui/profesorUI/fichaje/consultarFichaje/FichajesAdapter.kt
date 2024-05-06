package com.example.estechapp.ui.profesorUI.fichaje.consultarFichaje

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.R
import com.example.estechapp.databinding.ItemConsultaFichajeBinding
import com.example.estechapp.ui.profesorUI.Fichaje

class FichajesAdapter(private val fichajes: List<Fichaje>) : RecyclerView.Adapter<FichajesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemConsultaFichajeBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(fichaje: Fichaje){
            with(binding){
                ano.text = fichaje.ano.toString()
                dia.text = fichaje.dia.toString()
                mes.text = fichaje.mes
                hora.text = fichaje.hora
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConsultaFichajeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = fichajes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fichajes[position])
        if (position % 2 == 0){
            val gris = ContextCompat.getColor(holder.itemView.context, R.color.gris)
            holder.binding.bg.setBackgroundColor(gris)
        } else {
            val blanco = ContextCompat.getColor(holder.itemView.context, R.color.white)
            holder.binding.bg.setBackgroundColor(blanco)
        }
    }
}