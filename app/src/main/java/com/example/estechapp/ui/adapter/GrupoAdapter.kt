package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.R
import com.example.estechapp.data.models.Grupos
import com.example.estechapp.data.models.UserConRol
import com.example.estechapp.databinding.ItemGruposCuadradoBinding

class GrupoAdapter(private val grupos: List<Grupos>) : RecyclerView.Adapter<GrupoAdapter.ViewHolder>() {

    interface NavegarGrupoListener {
        fun navegarGrupo(item: Grupos)
    }

    var navegarGrupoListener: NavegarGrupoListener? = null

    class ViewHolder(val binding: ItemGruposCuadradoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(grupos: Grupos){
            with(binding){
                nombreGrupo.setText(grupos.name)
                cantidadAlumnos.setText(grupos.cantidad.toString())
                imageView3.setImageResource(R.drawable.graduate)
                imagen.setImageResource(R.drawable.curso_img)
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
        holder.bind(grupos[position])

        holder.binding.root.setOnClickListener {
            navegarGrupoListener?.navegarGrupo((grupos[position]))
        }
    }

}