package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.R
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.databinding.ItemTutoriasHotBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TutoriasHotAdapter(private val tutorias: List<DataMentoringResponse>) :
    RecyclerView.Adapter<TutoriasHotAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTutoriasHotBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tutoria: DataMentoringResponse) {
            //Esto hace que si eres el profesor te muestre el nombre y apellido del alumno.
            //Y si eres alumno te muestre el nombre y apellido del profesor.
            if (tutoria.studentAndroid == false) {
                binding.nombre.text = tutoria.student!!.name + " " + tutoria.student!!.lastname
            } else {
                binding.nombre.text = tutoria.teacher!!.name + " " + tutoria.teacher!!.lastname
            }
            //No puedo llegar a curso porque esta en la otra punta de la base de datos y tampoco tengo permisos.
            binding.curso.text = ""
            binding.textView10.text = ""
            //Aqui uso el roomName en vez de roomId
            binding.aula.text = tutoria.roomName

            // Convierte la cadena de texto a un objeto Date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            // La zona horaria se establece a la del sistema por defecto
            dateFormat.timeZone = TimeZone.getDefault()
            val start = dateFormat.parse(tutoria.start)

            // Formatea la fecha a una cadena de texto que solo incluye la hora y los minutos
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeFormat.timeZone = TimeZone.getTimeZone("UTC")
            val startTime = timeFormat.format(start)

            binding.fecha.text = startTime
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
        //Aqui muestra con fondo gris y va alternando con fondo blanco.
        //Para poder diferenciarlos mejor.
        if (position % 2 == 0) {
            val gris = ContextCompat.getColor(holder.itemView.context, R.color.gris)
            holder.binding.bg.setBackgroundColor(gris)
        } else {
            val blanco = ContextCompat.getColor(holder.itemView.context, R.color.white)
            holder.binding.bg.setBackgroundColor(blanco)
        }
    }

}