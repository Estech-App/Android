package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.data.models.DataTimeTableResponse
import com.example.estechapp.databinding.ItemHorarioBinding
import com.example.estechapp.data.models.Horario
import java.text.SimpleDateFormat
import java.util.Locale

class HorarioAdapter(private val horario: List<DataTimeTableResponse>) : RecyclerView.Adapter<HorarioAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHorarioBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(horario: DataTimeTableResponse){
            with(binding){
                val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
                val startDate = formato.parse(horario.start)
                val startHora = formatoHora.format(startDate)
                val endDate = formato.parse(horario.end)
                val endHora = formatoHora.format(endDate)
                horaHorario.text = startHora + " a " + endHora
                nombreAsignatura.text = horario.moduleName
                cursoHorario.text = horario.groupName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHorarioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = horario.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(horario[position])
        val clase = horario[position]
        /*if (clase){
            if (position % 2 == 0){
                val gris = ContextCompat.getColor(holder.itemView.context, R.color.gris)
                holder.binding.bg.setBackgroundColor(gris)
            } else {
                val blanco = ContextCompat.getColor(holder.itemView.context, R.color.white)
                holder.binding.bg.setBackgroundColor(blanco)
            }
        }*/

    }
}