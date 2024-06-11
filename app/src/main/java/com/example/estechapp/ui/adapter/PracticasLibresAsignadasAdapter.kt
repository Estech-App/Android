package com.example.estechapp.ui.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.R
import com.example.estechapp.data.models.DataFreeUsageModel
import com.example.estechapp.data.models.DataFreeUsageResponse
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.databinding.ItemTutoriasAsignadasAlumnoBinding
import java.util.Locale

class PracticasLibresAsignadasAdapter(private val practicaLibre: List<DataFreeUsageResponse>) :
    RecyclerView.Adapter<PracticasLibresAsignadasAdapter.ViewHolder>() {

    interface EliminarPracticaLibreListener {
        fun eliminarPracticaLibre(item: DataFreeUsageResponse)
    }

    var eliminarPracticaLibreListener: EliminarPracticaLibreListener? = null

    class ViewHolder(val binding: ItemTutoriasAsignadasAlumnoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(practicaLibre: DataFreeUsageResponse) {
            with(binding) {
                //Esto hace que si eres el profesor te muestre el nombre y apellidos del alumno.
                //Y si eres el alumno que te muestre el nombre y apellidos del profesor.
                binding.nombreAlumnoTutoria.text =
                    practicaLibre.user!!.name + " " + practicaLibre.user.lastname
                // Formatear la fecha
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val outputFormat = SimpleDateFormat("d/M", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val start = inputFormat.parse(practicaLibre.start)
                val startTime = inputFormat.parse(practicaLibre.start)
                val endTime = inputFormat.parse(practicaLibre.end)
                diaTutoria.text = outputFormat.format(start)

                aulaTutoria.text = practicaLibre.room.name
                horaTutoria.text = timeFormat.format(startTime) + " - " + timeFormat.format(endTime)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTutoriasAsignadasAlumnoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = practicaLibre.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(practicaLibre[position])
        if (position % 2 == 0) {
            val gris = ContextCompat.getColor(holder.itemView.context, R.color.gris)
            holder.binding.bg.setBackgroundColor(gris)
        } else {
            val blanco = ContextCompat.getColor(holder.itemView.context, R.color.white)
            holder.binding.bg.setBackgroundColor(blanco)
        }

        holder.binding.denegar.setOnClickListener {
            eliminarPracticaLibreListener?.eliminarPracticaLibre(practicaLibre[position])
        }
    }
}