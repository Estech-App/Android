package com.example.estechapp.ui.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.R
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.databinding.ItemTutoriasHotBinding
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.estechapp.data.models.Tutoria
import com.example.estechapp.ui.MyViewModel
import java.util.TimeZone

class TutoriasHotAdapter(private val tutorias: List<DataMentoringResponse>) :
    RecyclerView.Adapter<TutoriasHotAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTutoriasHotBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tutoria: DataMentoringResponse) {
            binding.nombre.text = tutoria.studentId.toString()
            binding.curso.text = "GET Cursos"
            binding.aula.text = tutoria.roomId.toString()

            // Convierte la cadena de texto a un objeto Date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            // La zona horaria se establece a la del sistema por defecto
            dateFormat.timeZone = TimeZone.getDefault()
            val date = dateFormat.parse(tutoria.date)

            // Formatea la fecha a una cadena de texto que solo incluye la hora y los minutos
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeFormat.timeZone = TimeZone.getTimeZone("UTC")
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