package com.example.estechapp.ui.profesorUI.fichaje

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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.data.models.DataRoomModel
import com.example.estechapp.data.models.DataTimeTableResponse
import com.example.estechapp.ui.adapter.HorarioAdapter
import com.example.estechapp.ui.adapter.TutoriasHotAdapter

class FichajeFragment : Fragment() {

    private var _binding: FragmentFichajeBinding? = null

    private lateinit var adapter2: HorarioAdapter

    private lateinit var adapter3: HorarioAdapter

    private lateinit var roomNames: Map<Int, String?>

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recibo los datos.
        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = pref.getString("username", "")
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        //Voy preparando el recyclerview
        val recyclerView = binding.recyclerTutoriasHot
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        //Voy preparando el recyclerview
        val recyclerView2 = binding.recyclerViewhorario1
        val llm2 = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = llm2

        //Voy preparando el recyclerview
        val recyclerView3 = binding.recyclerViewHorario2
        val llm3 = LinearLayoutManager(requireContext())
        recyclerView3.layoutManager = llm3

        viewModel.getRoomList("Bearer $token")

        viewModel.getMentoringTeacher("Bearer $token", id)

        viewModel.getModuleList("Bearer $token")

        viewModel.getGroupUser("Bearer $token", id)

        viewModel.getTimeTableALlList("Bearer $token")

        binding.textView4.text = user

        //Esto es para que se actualice la fecha y la hora y si el checkin es entrada o salida
        //cada 0.1 segundo porque con 1 segundo daba tiempo a darle al checkin 2 veces y
        //fichar de entrada 2 veces o salida 2 veces.
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

                    binding.textView6.text = horaMinutos

                    val token = pref.getString("token", "")

                    val id = pref.getInt("id", 0)

                    //Aqui hago el getCheckin para saber cual fue el ultimo checkin y
                    //decirle si fue entrada que al hacer click se haga el de salida y viceversa.
                    if (token != null) {

                        viewModel.getCheckIn("Bearer $token", id)

                    }

                    //Si es true se hace la entrada al hacer checkin y si es false se hace la salida.
                    val checkIn = pref.getBoolean(
                        "checking",
                        true
                    )

                    //Esto lo he puesto dentro del bucle para que al hacer checkin
                    //se cambie de entrada a salida y de salida a entrada.
                    if (checkIn) {
                        //Este es para hacer entrada.
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
                                //Este es el AlertDialog.
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
                        //Este es para hacer salida.
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
                                //Este es el AlertDialog.
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

                    handler.postDelayed(this, 100)

                }
            }
        }

        handler.post(runnable)
        //Este boton es para ver los ultimos fichajes.
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_fichaje_to_consultaFichajeFragment)
        }

        viewModel.liveDataCheckIn.observe(viewLifecycleOwner, Observer {
            //Si es true hace la entrada y si es false la salida.
            val checkIn = pref.getBoolean("checking", true)
            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            //Si es true se pone a false.
            if (checkIn) {
                val editor = pref.edit()
                editor.putBoolean("checking", false)
                editor.commit()
            } else {
                //Si es false se pone a true.
                val editor = pref.edit()
                editor.putBoolean("checking", true)
                editor.commit()
                val textview = view2.findViewById<TextView>(R.id.textView16)
                textview.setText("Salida registrada con exito!")
            }
            //Un AlertDialog para que sepas que se realizo correctamente.
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

        //Esto pasa cuando el checkin da error, te muestra otro alertDialog pero en rojo.
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

        //Esto es para recibir todos los checkin y saber si no hay hace la entrada
        //Y si el ultimo fue entrada hace salida y si fue salida hace entrada.
        viewModel.liveDataCheckInList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    val editor = pref.edit()
                    editor.putBoolean("checking", true)
                    editor.commit()
                } else {
                    val ultimoCheckin = it[it.size - 1]
                    if (ultimoCheckin.checkIn == true) {
                        val editor = pref.edit()
                        editor.putBoolean("checking", false)
                        editor.commit()
                    } else {
                        val editor = pref.edit()
                        editor.putBoolean("checking", true)
                        editor.commit()
                    }
                }
            }
        })

        viewModel.liveDataRoomAndMentoring.observe(viewLifecycleOwner, Observer { pair ->
            val roomList = pair.first
            val mentoringList = pair.second

            roomNames =
                roomList?.associateBy({ it.id }, { it.id?.run { it.name } ?: null }) ?: emptyMap()

            var mentoringModel = ArrayList<DataRoomModel>()

            val ahora = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val formato =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            for (mentoring in mentoringList) {
                if (mentoring.roomId == null) {
                    mentoringModel.add(DataRoomModel(null, null))
                } else {
                    mentoringModel.add(DataRoomModel(mentoring.roomId, null))
                }
            }

            for (mentoring in mentoringModel) {
                if (mentoring.roomId == null) {
                    mentoring.roomName = null
                } else {
                    val roomName = roomNames[mentoring.roomId]
                    if (roomName != null) {
                        mentoring.roomName = roomName
                    }
                }
            }

            for (i in mentoringList.indices) {
                if (mentoringModel[i].roomName == null) {
                    mentoringList[i].roomName = null
                } else {
                    mentoringList[i].roomName = mentoringModel[i].roomName
                }
            }

            //Aqui si es pending se va a PendientesAdapter
            //Y si es approved o modified se van a AsignadasAdapter
            val pendientes = mentoringList.filter {
                it.status == "PENDING" && (formato.parse(it.start)
                    .equals(ahora.time) || formato.parse(it.start).after(ahora.time))
            }
            val otras = mentoringList.filter {
                (it.status == "APPROVED" || it.status == "MODIFIED") && (formato.parse(it.start)
                    .equals(ahora.time) || formato.parse(it.start).after(ahora.time))
            }

            // Ordena las listas por la fecha de inicio
            val pendientesOrdenadas = pendientes.sortedBy { formato.parse(it.start) }
            val otrasOrdenadas = otras.sortedBy { formato.parse(it.start) }

            // Asigna las listas ordenadas al adaptador
            val adapter = TutoriasHotAdapter(otrasOrdenadas)
            recyclerView.adapter = adapter

        })

        viewModel.liveDataGroupUserModuleTimeTable.observe(viewLifecycleOwner, Observer {
            val GroupList = it.first
            val ModuleList = it.second
            val TimeTableList = it.third

            var horarioManana: MutableList<DataTimeTableResponse> = mutableListOf()
            var horarioTarde: MutableList<DataTimeTableResponse> = mutableListOf()

            var hoy = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            hoy -= 1
            hoy.toString()

            for (timeTable in TimeTableList) {
                for (grupo in GroupList) {
                    for (modulo in ModuleList) {
                        if (timeTable.schoolGroupId == grupo.id && timeTable.moduleId == modulo.id && modulo.usersName.contains(
                                user
                            ) && timeTable.weekday == hoy.toString()
                        ) {
                            timeTable.groupName = grupo.name
                            timeTable.moduleName = modulo.name
                            if (grupo.evening == false) {
                                horarioManana.add(timeTable)
                            } else {
                                horarioTarde.add(timeTable)
                            }
                        }
                    }
                }
            }
            // Ordenar las listas por fecha
            horarioManana.sortWith(compareBy { it.start }) // Reemplaza 'fecha' con el nombre de tu campo de fecha
            horarioTarde.sortWith(compareBy { it.start }) // Reemplaza 'fecha' con el nombre de tu campo de fecha

            adapter2 = HorarioAdapter(horarioManana)
            recyclerView2.adapter = adapter2

            adapter3 = HorarioAdapter(horarioTarde)
            recyclerView3.adapter = adapter3
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}