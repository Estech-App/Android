package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.databinding.ItemGruposCuadradoBinding

/*class GrupoAdapter(private val grupos: List<Grupo>) : RecyclerView.Adapter<GrupoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemGruposCuadradoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(grupo: Grupo){
            with(binding){
                nombreGrupo.text = grupo.nombre
                cantidadAlumnos.text = grupo.alumnos.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGruposCuadradoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = grupos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}*/