package com.example.estechapp.ui.alumnoUI.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.databinding.FragmentHomeAlumnoBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.TutoriasHotAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class HomeAlumnoFragment : Fragment() {

    private var _binding: FragmentHomeAlumnoBinding? = null

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeAlumnoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recibo todos los datos.
        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = pref.getString("username", "")
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        //Voy preparando el recyclerview
        val recyclerView = binding.recyclerTutoriasHot
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        viewModel.getMentoringStudent("Bearer $token", id)

        binding.textView4.text = user

        //Esto es para que se actualice la fecha y la hora cada segundo.
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {

                _binding?.let { binding ->

                    val calendar = Calendar.getInstance()
                    val tz = TimeZone.getTimeZone("GMT+2")
                    calendar.setTimeZone(tz)

                    val formatoHora = SimpleDateFormat("HH:mm", Locale.US)
                    formatoHora.timeZone = tz

                    val diaMes = calendar.get(Calendar.DAY_OF_MONTH)

                    val diaSemana = calendar.get(Calendar.DAY_OF_WEEK)

                    var diaSemanaN = ""

                    val mes = calendar.get(Calendar.MONTH)

                    var mesN = ""

                    //Esto es para saber que numero es cada dia.
                    when (diaSemana) {
                        1 -> {
                            diaSemanaN = "Domingo"
                        }

                        2 -> {
                            diaSemanaN = "Lunes"
                        }

                        3 -> {
                            diaSemanaN = "Martes"
                        }

                        4 -> {
                            diaSemanaN = "Miercoles"
                        }

                        5 -> {
                            diaSemanaN = "Jueves"
                        }

                        6 -> {
                            diaSemanaN = "Viernes"
                        }

                        7 -> {
                            diaSemanaN = "Sabado"
                        }
                    }

                    //Esto es para saber que numero es cada mes.
                    when (mes) {
                        0 -> {
                            mesN = "Enero"
                        }

                        1 -> {
                            mesN = "Febrero"
                        }

                        2 -> {
                            mesN = "Marzo"
                        }

                        3 -> {
                            mesN = "Abril"
                        }

                        4 -> {
                            mesN = "Mayo"
                        }

                        5 -> {
                            mesN = "Junio"
                        }

                        6 -> {
                            mesN = "Julio"
                        }

                        7 -> {
                            mesN = "Agosto"
                        }

                        8 -> {
                            mesN = "Septiembre"
                        }

                        9 -> {
                            mesN = "Octubre"
                        }

                        10 -> {
                            mesN = "Noviembre"
                        }

                        11 -> {
                            mesN = "Diciembre"
                        }
                    }

                    binding.textView5.text = "Tu dia hoy, $diaSemanaN $diaMes de $mesN"

                }
            }
        }

        handler.post(runnable)

        viewModel.liveDataMentoringList.observe(viewLifecycleOwner, Observer { it ->
            //Esto es para recibir todas las tutorias por id.
            if (it != null) {
                val calendar = Calendar.getInstance()
                // La zona horaria se establece a la del sistema por defecto
                val tz = TimeZone.getDefault()
                calendar.timeZone = tz
                // Asegúrate de que el calendario comienza a las 00:00:00
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                // La zona horaria se establece a la del sistema por defecto
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                //Si es alumno lo pone a true.
                val editor = pref.edit()
                editor.putBoolean("student", true)
                editor.commit()

                val filteredMentorings = it.filter {
                    //Aqui va poniendo el booleano de student y el roomName.
                    if (it.roomId == null) {
                        it.roomName = null
                    } else {
                        viewModel.getRoomId("Bearer $token", it.roomId)
                        it.roomName = pref.getString("room", "")!!
                    }
                    it.studentAndroid = pref.getBoolean("student", true)
                    val mentoringCalendar = Calendar.getInstance()
                    mentoringCalendar.timeZone = tz
                    // Convierte la cadena de texto a un objeto Date
                    val start = dateFormat.parse(it.start)
                    mentoringCalendar.time = start

                    val mentoringYear = mentoringCalendar.get(Calendar.YEAR)
                    val mentoringMonth = mentoringCalendar.get(Calendar.MONTH)
                    val mentoringDay = mentoringCalendar.get(Calendar.DAY_OF_MONTH)

                    //Esto muestra las tutorias approved o modified de hoy.
                    val isToday = mentoringYear == calendar.get(Calendar.YEAR) &&
                            mentoringMonth == calendar.get(Calendar.MONTH) &&
                            mentoringDay == calendar.get(Calendar.DAY_OF_MONTH)

                    val isStatusValid = it.status == "APPROVED" || it.status == "MODIFIED"

                    isToday && isStatusValid
                }

                val adapter = TutoriasHotAdapter(filteredMentorings)
                recyclerView.adapter = adapter

            }
        })

        //Esto es para con el roomId recibir el nombre del aula.
        viewModel.liveDataRoom.observe(viewLifecycleOwner, Observer { it ->
            if (it != null) {
                val editor = pref.edit()
                editor.putString("room", it.name)
                editor.commit()
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}