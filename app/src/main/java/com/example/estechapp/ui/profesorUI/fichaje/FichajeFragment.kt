package com.example.estechapp.ui.profesorUI.fichaje

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.estechapp.databinding.FragmentFichajeBinding
import com.example.estechapp.ui.MyViewModel
import java.util.Calendar
import java.util.*
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import com.example.estechapp.ui.profesorUI.Tutoria

class FichajeFragment : Fragment() {

    private var _binding: FragmentFichajeBinding? = null

    //private val viewModel by viewModels<MyViewModel>()

    private val viewModel by viewModels<MyViewModel>{
        MyViewModel.MyViewModelFactory(requireContext())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFichajeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTutoriasHot.adapter = TutoriasHotAdapter(
            listOf(
                Tutoria("Ramon", "DAM 2º", "Aula DAM")
            )
        )

        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = pref.getString("username", "")

        binding.textView4.text = user

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {

                _binding?.let { binding ->

                    val calendar = Calendar.getInstance()

                    val formatoHora = SimpleDateFormat("HH:mm", Locale.US)
                    val horaMinutos = formatoHora.format(calendar.time)

                    val diaMes = calendar.get(Calendar.DAY_OF_MONTH)

                    val diaSemana = calendar.get(Calendar.DAY_OF_WEEK)

                    var diaSemanaN = ""

                    val mes = calendar.get(Calendar.MONTH)

                    var mesN = ""

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

                    binding.textView6.text = horaMinutos

                    handler.postDelayed(this, 1000)

                }
            }
        }

        handler.post(runnable)

        binding.imageButton.setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val calendar = Calendar.getInstance()
            dateFormat.timeZone = TimeZone.getTimeZone("GMT+2")
            val formatDate = dateFormat.format(calendar.time)
            val date = dateFormat.parse(formatDate)

            val token = pref.getString("token", "")
            val id = pref.getInt("id", 0)
            val name = pref.getString("username", "")
            val lastname = pref.getString("lastname", "")

            if (token != null && date != null && name != null && lastname != null) {

                viewModel.postCheckIn("Bearer $token", date, true, id, name, lastname)

                viewModel.liveDataCheckIn.observe(viewLifecycleOwner, Observer {
                    
                })

                viewModel.liveDataCheckInError.observe(viewLifecycleOwner, Observer {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}