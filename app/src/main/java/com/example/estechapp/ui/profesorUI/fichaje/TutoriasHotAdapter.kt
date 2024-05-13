package com.example.estechapp.ui.profesorUI.fichaje

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.R
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.databinding.ItemTutoriasHotBinding
import com.example.estechapp.ui.profesorUI.Tutoria
import java.text.SimpleDateFormat
import java.util.Locale

class TutoriasHotAdapter(private val tutorias: List<DataMentoringResponse>) :
    RecyclerView.Adapter<TutoriasHotAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTutoriasHotBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tutoria: DataMentoringResponse) {
            binding.nombre.text = tutoria.studentId.toString()
            binding.curso.text = "GET Cursos"
            binding.aula.text = tutoria.roomId.toString()

            // Convierte la cadena de texto a un objeto Date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
            val date = dateFormat.parse(tutoria.date)

            // Formatea la fecha a una cadena de texto que solo incluye la hora y los minutos
            val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
            val time = timeFormat.format(date)

            binding.fecha.text = time
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTutoriasHotBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = tutorias.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tutorias[position])
        if (position % 2 == 0) {
            val gris = ContextCompat.getColor(holder.itemView.context, R.color.gris)
            holder.binding.bg.setBackgroundColor(gris)
        } else {
            val blanco = ContextCompat.getColor(holder.itemView.context, R.color.white)
            holder.binding.bg.setBackgroundColor(blanco)
        }
    }

}