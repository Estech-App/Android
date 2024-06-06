package com.example.estechapp.ui.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.databinding.ItemTutoriasAsignadasBinding
import java.util.Locale

class TutoriasAsignadasAdapter(private val tutoria: List<DataMentoringResponse>) :
    RecyclerView.Adapter<TutoriasAsignadasAdapter.ViewHolder>() {

        interface EliminarTutoriaListener {
            fun eliminarTutoria(item: DataMentoringResponse)
        }

    interface ModificarTutoriaListener {
        fun modificarTutoria(item: DataMentoringResponse)
    }

    var eliminarTutoriasListener: EliminarTutoriaListener? = null

    var modificarTutoriaListener: ModificarTutoriaListener? = null

    class ViewHolder(val binding: ItemTutoriasAsignadasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tutoria: DataMentoringResponse) {
            with(binding) {
                //Esto hace que si eres el profesor te muestre el nombre y apellidos del alumno.
                //Y si eres el alumno que te muestre el nombre y apellidos del profesor.
                if (tutoria.studentAndroid == false) {
                    binding.nombreAlumnoTutoria.text =
                        tutoria.student!!.name + " " + tutoria.student.lastname
                } else {
                    binding.nombreAlumnoTutoria.text =
                        tutoria.teacher!!.name + " " + tutoria.teacher.lastname
                }

                // Formatear la fecha
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val outputFormat = SimpleDateFormat("d/M", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val start = inputFormat.parse(tutoria.start)
                val startTime = inputFormat.parse(tutoria.start)
                val endTime = inputFormat.parse(tutoria.end)
                diaTutoria.text = outputFormat.format(start)

                aulaTutoria.text = tutoria.roomName
                horaTutoria.text = timeFormat.format(startTime) + " - " + timeFormat.format(endTime)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTutoriasAsignadasBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = tutoria.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tutoria[position])

        holder.binding.denegar.setOnClickListener {
            eliminarTutoriasListener?.eliminarTutoria(tutoria[position])
        }

        holder.binding.editar.setOnClickListener {
            modificarTutoriaListener?.modificarTutoria(tutoria[position])
        }
    }
}