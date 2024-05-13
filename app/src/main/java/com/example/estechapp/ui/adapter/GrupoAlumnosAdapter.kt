package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.databinding.ItemListaAlumnoBinding
import com.example.estechapp.data.models.Alumno

class GrupoAlumnosAdapter(private val alumno: List<Alumno>) : RecyclerView.Adapter<GrupoAlumnosAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemListaAlumnoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(alumno: Alumno){
            binding.nombreAlumno.text = alumno.nombre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListaAlumnoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = alumno.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.orden.text = position.toString()
    }
}