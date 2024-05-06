package com.example.estechapp.ui.profesorUI.fichaje

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.estechapp.R
import com.example.estechapp.databinding.FragmentFichajeBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.profesorUI.Tutoria
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class FichajeFragment : Fragment() {

    private var _binding: FragmentFichajeBinding? = null

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFichajeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTutoriasHot.adapter = TutoriasHotAdapter(
            listOf(
                Tutoria("Ramon", "DAM 2º", "Aula DAM"),
                Tutoria("Juan", "DOWN 2º", "Aula DOWN"),
                Tutoria("Fernando", "DAM 2º", "Aula DAM")
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
                    val tz = TimeZone.getTimeZone("GMT+2")
                    calendar.setTimeZone(tz)

                    val formatoHora = SimpleDateFormat("HH:mm", Locale.US)
                    formatoHora.timeZone = tz
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

                    val checkIn = pref.getBoolean(
                        "checking",
                        true
                    ) // Neceseitor un getallchekingsby(id) y pikear el ultimo para saber cual es. entrar o salir.

                    if (checkIn) {
                        binding.imageButton.setImageResource(R.drawable.entrada_icon)

                        binding.imageButton.setOnClickListener {
                            val calendar = Calendar.getInstance()
                            val tz = TimeZone.getTimeZone("GMT+2")
                            calendar.setTimeZone(tz)
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                            dateFormat.timeZone = tz
                            val Date = dateFormat.format(calendar.time)

                            val token = pref.getString("token", "")
                            val id = pref.getInt("id", 0)
                            val name = pref.getString("username", "")
                            val lastname = pref.getString("lastname", "")

                            if (token != null && name != null && lastname != null) {

                                val builder = AlertDialog.Builder(requireContext())
                                val view = layoutInflater.inflate(R.layout.alert, null)
                                builder.setView(view)
                                val dialog = builder.create()
                                dialog.show()
                                val cancelar = view.findViewById<Button>(R.id.button)
                                val registrar = view.findViewById<Button>(R.id.button4)
                                val titulo = view.findViewById<TextView>(R.id.textView7)
                                val mensaje = view.findViewById<TextView>(R.id.textView15)
                                titulo.setText("Alert")
                                mensaje.setText("¿Esta seguro de registrar esta entrada del fichaje?")
                                cancelar.setOnClickListener {
                                    dialog.dismiss()
                                }
                                registrar.setOnClickListener {

                                    viewModel.postCheckIn(
                                        "Bearer $token",
                                        Date,
                                        checkIn,
                                        id,
                                        name,
                                        lastname
                                    )

                                    dialog.dismiss()

                                }
                            }
                        }

                    } else {

                        binding.imageButton.setImageResource(R.drawable.salida_icon)

                        binding.imageButton.setOnClickListener {
                            val calendar = Calendar.getInstance()
                            val tz = TimeZone.getTimeZone("GMT+2")
                            calendar.setTimeZone(tz)
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                            dateFormat.timeZone = tz
                            val Date = dateFormat.format(calendar.time)

                            val token = pref.getString("token", "")
                            val id = pref.getInt("id", 0)
                            val name = pref.getString("username", "")
                            val lastname = pref.getString("lastname", "")

                            if (token != null && name != null && lastname != null) {

                                val builder = AlertDialog.Builder(requireContext())
                                val view = layoutInflater.inflate(R.layout.alert, null)
                                builder.setView(view)
                                val dialog = builder.create()
                                dialog.show()
                                val cancelar = view.findViewById<Button>(R.id.button)
                                val registrar = view.findViewById<Button>(R.id.button4)
                                val titulo = view.findViewById<TextView>(R.id.textView7)
                                val mensaje = view.findViewById<TextView>(R.id.textView15)
                                titulo.setText("Alert")
                                mensaje.setText("¿Esta seguro de registrar la salida?")
                                cancelar.setOnClickListener {
                                    dialog.dismiss()
                                }
                                registrar.setOnClickListener {

                                    viewModel.postCheckIn(
                                        "Bearer $token",
                                        Date,
                                        checkIn,
                                        id,
                                        name,
                                        lastname
                                    )

                                    dialog.dismiss()

                                }
                            }
                        }

                    }

                    handler.postDelayed(this, 1000)

                }
            }
        }

        handler.post(runnable)

        binding.login.setOnClickListener {
            /*val builder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.alert3, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()*/
            /*val builder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.dialog, null)

            builder.setView(view)

            val dialog = builder.create()
            dialog.show()

            val button = view.findViewById<Button>(R.id.button3)
            button.setText("Hola")
            button.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    requireContext(),
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Aquí puedes manejar la fecha seleccionada
                        Toast.makeText(
                            requireContext(),
                            "$dayOfMonth/${monthOfYear + 1}/$year",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    year,
                    month,
                    day
                )

                dpd.show() // Esto mostrará el DatePickerDialog
            }

            // Primero, define los elementos del Spinner en un array
            val elementos = arrayOf("Elige", "Elemento 1", "Elemento 2", "Elemento 3")

// Crea un ArrayAdapter usando el array de strings y el layout predeterminado del Spinner
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, elementos)

// Especifica el layout a usar cuando aparece la lista de opciones
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Aplica el adaptador al Spinner
            val spinner = view.findViewById<Spinner>(R.id.spinner1)
            spinner.adapter = adapter

// Define el comportamiento cuando se selecciona un elemento
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val elementoSeleccionado = parent.getItemAtPosition(position) as String
                    // Aquí puedes manejar el elemento seleccionado
                    Toast.makeText(
                        requireContext(),
                        "Seleccionaste: $elementoSeleccionado",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Aquí puedes manejar cuando no se selecciona ningún elemento
                }
            }

            val button1 = view.findViewById<Button>(R.id.button1)

            button1.setOnClickListener {

            val tpd = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    // Aquí puedes manejar la hora seleccionada
                    Toast.makeText(
                        requireContext(),
                        "$hourOfDay:$minute",
                        Toast.LENGTH_LONG
                    ).show()
                },
                12, 0, true
            )
            tpd.show()

                dialog.dismiss()
            }

            val button2 = view.findViewById<Button>(R.id.button2)

            button2.setOnClickListener {
                dialog.dismiss()

                val builder2 = AlertDialog.Builder(requireContext())
                val view2 = layoutInflater.inflate(
                    R.layout.dialog2,
                    null
                ) // Asegúrate de cambiar esto al layout correcto

                builder2.setView(view2)

                val dialog2 = builder2.create()
                dialog2.show()

                // Cerrar el segundo diálogo después de 3 segundos
                Handler(Looper.getMainLooper()).postDelayed({
                    if (dialog2.isShowing) {
                        dialog2.dismiss()
                    }
                }, 3000) // 3000 milisegundos equivalen a 3 segundos
            }*/
        }

        viewModel.liveDataCheckIn.observe(viewLifecycleOwner, Observer {
            val checkIn = pref.getBoolean("checking", true)
            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            if (checkIn) {
                val editor = pref.edit()
                editor.putBoolean("checking", false)
                editor.commit()
            } else {
                val editor = pref.edit()
                editor.putBoolean("checking", true)
                editor.commit()
                val textview = view2.findViewById<TextView>(R.id.textView16)
                textview.setText("Salida registrada con exito!")//viva el betis
            }
            builder2.setView(view2)
            val dialog2 = builder2.create()
            dialog2.show()
            dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog2.isShowing) {
                    dialog2.dismiss()
                }
            }, 5000)
        })

        viewModel.liveDataCheckInError.observe(viewLifecycleOwner, Observer {
            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            val textview = view2.findViewById<TextView>(R.id.textView16)
            textview.setBackgroundResource(R.drawable.rounded_textview_error)
            textview.text = "Se ha producido un error al registrar la entrada!"
            builder2.setView(view2)
            val dialog2 = builder2.create()
            dialog2.show()
            dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog2.isShowing) {
                    dialog2.dismiss()
                }
            }, 5000)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}