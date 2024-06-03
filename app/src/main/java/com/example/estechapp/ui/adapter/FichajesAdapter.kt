package com.example.estechapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estechapp.R
import com.example.estechapp.data.models.DataCheckInResponse
import com.example.estechapp.data.models.FichajePair
import com.example.estechapp.databinding.ItemConsultaFichajeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.estechapp.data.models.Fichaje

class FichajesAdapter(private val fichajes: List<FichajePair>) :
    RecyclerView.Adapter<FichajesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemConsultaFichajeBinding) :
        RecyclerView.ViewHolder(binding.root) {

            //Aqui recibe el fichajepair.
        fun bind(fichajePair: FichajePair) {
            with(binding) {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                //Esto hace que si solo esta true ponga los datos y solo la hora que hizo la entrada.
                fichajePair.fichajeTrue?.let { fichaje ->
                    val date = sdf.parse(fichaje.date)
                    val calendar = Calendar.getInstance()
                    calendar.time = date

                    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                    val monthFormat = SimpleDateFormat("MM", Locale.getDefault())
                    val monthString = monthFormat.format(calendar.time)
                    val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
                    var mesText = ""

                    //Esto es para saber que numero es cada mes.
                    when (monthString) {
                        "01" -> {
                            mesText = "Enero"
                        }

                        "02" -> {
                            mesText = "Febrero"
                        }

                        "03" -> {
                            mesText = "Marzo"
                        }

                        "04" -> {
                            mesText = "Abril"
                        }

                        "05" -> {
                            mesText = "Mayo"
                        }

                        "06" -> {
                            mesText = "Junio"
                        }

                        "07" -> {
                            mesText = "Julio"
                        }

                        "08" -> {
                            mesText = "Agosto"
                        }

                        "09" -> {
                            mesText = "Septiembre"
                        }

                        "10" -> {
                            mesText = "Octubre"
                        }

                        "11" -> {
                            mesText = "Noviembre"
                        }

                        "12" -> {
                            mesText = "Diciembre"
                        }
                    }

                    ano.text = yearFormat.format(calendar.time)
                    mes.text = mesText
                    dia.text = dayFormat.format(calendar.time)

                    val hourTrue = hourFormat.format(calendar.time)
                    //Esto hace que cuando tambien esta false aparte del true ponga a que hora
                    //empezo y acabo.
                    fichajePair.fichajeFalse?.let { fichajeFalse ->
                        val dateFalse = sdf.parse(fichajeFalse.date)
                        val calendarFalse = Calendar.getInstance()
                        calendarFalse.time = dateFalse
                        val hourFalse = hourFormat.format(calendarFalse.time)
                        hora.text = "$hourTrue a $hourFalse"
                    } ?: run {
                        hora.text = hourTrue
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConsultaFichajeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount() = fichajes.size

    //Esto hace que se vean con fondo gris va alternando con fondo blanco.
    //Para poder diferenciarlos mejor.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fichajes[position])
        if (position % 2 == 0) {
            val gris = ContextCompat.getColor(holder.itemView.context, R.color.gris)
            holder.binding.bg.setBackgroundColor(gris)
        } else {
            val blanco = ContextCompat.getColor(holder.itemView.context, R.color.white)
            holder.binding.bg.setBackgroundColor(blanco)
        }
    }
}
