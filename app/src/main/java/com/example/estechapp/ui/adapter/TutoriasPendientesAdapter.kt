package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.databinding.ItemTutoriasBinding
import com.example.estechapp.data.models.Tutoria

class TutoriasPendientesAdapter(private val tutoria: List<Tutoria>) : RecyclerView.Adapter<TutoriasPendientesAdapter.ViewHolder>() {

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
        TODO("Not yet implemented")
    }
}