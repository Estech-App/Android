package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.data.models.UserConRol
import com.example.estechapp.data.models.UserNombres
import com.example.estechapp.databinding.ItemListaAlumnoBinding

class GrupoAlumnosAdapter2(private var alumno: List<UserConRol>) : RecyclerView.Adapter<GrupoAlumnosAdapter2.ViewHolder>(){

    interface EnviarCorreoListener {
        fun enviarCorreo(item: UserConRol)
    }

    var enviarCorreoListener: EnviarCorreoListener? = null

    class ViewHolder(val binding: ItemListaAlumnoBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(alumno: UserConRol){
            with(binding) {
                nombrealumno.setText(alumno.name + " " + alumno.lastname)
                orden.setText(alumno.posicion.toString())
            }
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
        holder.bind(alumno[position])

        holder.binding.root.setOnClickListener {
            enviarCorreoListener?.enviarCorreo(alumno[position])
        }
    }

    fun updateData(newData: List<UserConRol>) {
        this.alumno = newData
        notifyDataSetChanged()
    }

}