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
import com.example.estechapp.data.models.DataRoomModel
import com.example.estechapp.data.models.DataTimeTableResponse
import com.example.estechapp.databinding.FragmentHomeAlumnoBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.HorarioAdapter
import com.example.estechapp.ui.adapter.TutoriasHotAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class HomeAlumnoFragment : Fragment() {

    private var _binding: FragmentHomeAlumnoBinding? = null

    private lateinit var adapter2: HorarioAdapter

    private lateinit var adapter3: HorarioAdapter

    private lateinit var roomNames: Map<Int, String?>

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

        val recyclerView2 = binding.recyclerViewhorario1
        val llm2 = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = llm2

        val recyclerView3 = binding.recyclerViewhorario2
        val llm3 = LinearLayoutManager(requireContext())
        recyclerView3.layoutManager = llm3

        viewModel.getRoomList("Bearer $token")

        viewModel.getMentoringStudent("Bearer $token", id)

        viewModel.getModuleList("Bearer $token")

        viewModel.getGroupUser("Bearer $token", id)

        viewModel.getTimeTableALlList("Bearer $token")

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
                        if (timeTable.schoolGroupId == grupo.id && timeTable.moduleId == modulo.id && timeTable.weekday == hoy.toString()) {
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