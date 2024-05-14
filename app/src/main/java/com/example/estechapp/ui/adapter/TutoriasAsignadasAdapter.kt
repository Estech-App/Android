package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.databinding.ItemTutoriasBinding
import com.example.estechapp.data.models.Tutoria

class TutoriasAsignadasAdapter(private val tutoria: List<Tutoria>) : RecyclerView.Adapter<TutoriasAsignadasAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTutoriasBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(tutoria: Tutoria){
            with(binding){
                nombreAlumnoTutoria.text = tutoria.alumno
                diaTutoria.text = tutoria.dia
                aulaTutoria.text = tutoria.aula
                horaTutoria.text = tutoria.hora
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTutoriasBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = tutoria.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tutoria = tutoria[position]
        if (tutoria.isAprovado){

            //PREGUNTAR A SERGIO COMO HACER PARA FILTRAR LOS RESULTADOS Y ELIMINAR LOS QUE NO INTERESAN EN ESTE RECYCLER
        }
    }
}