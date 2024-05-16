package com.example.estechapp.ui.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.databinding.ItemTutoriasBinding
import com.example.estechapp.data.models.Tutoria
import java.util.Locale

class TutoriasAsignadasAdapter(private val tutoria: List<DataMentoringResponse>) : RecyclerView.Adapter<TutoriasAsignadasAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTutoriasBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(tutoria: DataMentoringResponse){
            with(binding){
                nombreAlumnoTutoria.text = tutoria.studentId.toString()

                // Formatear la fecha
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val outputFormat = SimpleDateFormat("d/M", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = inputFormat.parse(tutoria.date)
                val time = inputFormat.parse(tutoria.date)
                diaTutoria.text = outputFormat.format(date)

                aulaTutoria.text = tutoria.roomId.toString()
                horaTutoria.text = timeFormat.format(time)
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
        holder.bind(tutoria[position])
    }
}