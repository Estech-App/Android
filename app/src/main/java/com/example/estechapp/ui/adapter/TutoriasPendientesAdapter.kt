package com.example.estechapp.ui.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.databinding.ItemTutoriasPendientesBinding
import java.util.Locale

class TutoriasPendientesAdapter(private val tutoria: List<DataMentoringResponse>) :
    RecyclerView.Adapter<TutoriasPendientesAdapter.ViewHolder>() {

    interface EliminarTutoriaListener {
        fun eliminarTutoria(item: DataMentoringResponse)
    }

    interface ModificarTutoriaListener {
        fun modificarTutoria(item: DataMentoringResponse)
    }

    interface AsignarTutoriaListener {
        fun asignarTutoria(item: DataMentoringResponse)
    }

    var eliminarTutoriaListener: EliminarTutoriaListener? = null

    var modificarTutoriaListener: ModificarTutoriaListener? = null

    var asignarTutoriaListener: AsignarTutoriaListener? = null

    class ViewHolder(val binding: ItemTutoriasPendientesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tutoria: DataMentoringResponse) {
            with(binding) {
                nombreAlumnoTutoria.text = tutoria.student!!.name + " " + tutoria.student!!.lastname

                // Formatear la fecha
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val outputFormat = SimpleDateFormat("d/M", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val start = inputFormat.parse(tutoria.start)
                val startTime = inputFormat.parse(tutoria.start)
                val endTime = inputFormat.parse(tutoria.end)
                val startHora = timeFormat.format(startTime)
                val endHora = timeFormat.format(endTime)
                diaTutoria.text = outputFormat.format(start)

                if (tutoria.roomId == null) {
                    aulaTutoria.text = "Aula por asignar"
                } else {
                    aulaTutoria.text = tutoria.roomName
                }
                if (startHora != "00:00" && endHora != "00:00") {
                    horaTutoria.text =
                        startHora + " - " + endHora
                } else {
                    horaTutoria.text = "Hora por asignar"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTutoriasPendientesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = tutoria.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tutoria[position])

        holder.binding.denegar.setOnClickListener {
            eliminarTutoriaListener?.eliminarTutoria(tutoria[position])
        }

        holder.binding.editar.setOnClickListener {
            modificarTutoriaListener?.modificarTutoria(tutoria[position])
        }

        holder.binding.tick.setOnClickListener {
            asignarTutoriaListener?.asignarTutoria(tutoria[position])
        }
    }
}