package com.example.estechapp.ui.profesorUI.tutorias

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.R
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.data.models.DataRoomModel
import com.example.estechapp.databinding.FragmentTutoriasBinding
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapter
import com.example.estechapp.ui.adapter.TutoriasPendientesAdapter
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.HorarioAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TutoriasFragment : Fragment() {

    private var _binding: FragmentTutoriasBinding? = null

    private lateinit var adapter: TutoriasPendientesAdapter

    private lateinit var adapter2: TutoriasAsignadasAdapter

    private lateinit var rooms: Array<String>

    private lateinit var hours: Array<String>

    private lateinit var occupededHours: Array<Pair<String, String>>

    private lateinit var roomsId: Map<String, Int>

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

        _binding = FragmentTutoriasBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hours = arrayOf(
            "8:30",
            "9:00",
            "9:30",
            "10:00",
            "10:30",
            "11:00",
            "11:30",
            "12:00",
            "12:30",
            "13:00",
            "13:30",
            "14:00",
            "14:30",
            "15:30",
            "16:00",
            "16:30",
            "17:00",
            "17:30",
            "18:00",
            "18:30",
            "19:00",
            "19:30",
            "20:00",
            "20:30",
            "21:00",
            "21:30"
        )

        //Recibo los datos con el sharedPreferences
        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        //Voy preparando los recyclerView
        val recyclerView = binding.recyclerAsignadas
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        val recyclerView2 = binding.recyclerPendientes
        val llm2 = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = llm2

        var asign = false

        var modify = false

        viewModel.getRoomList("Bearer $token")

        viewModel.getMentoringTeacher("Bearer $token", id)

        viewModel.liveDataRoomAndMentoring.observe(viewLifecycleOwner, Observer { pair ->
            val roomList = pair.first
            val mentoringList = pair.second

            roomNames =
                roomList?.associateBy({ it.id }, { it.id?.run { it.name } ?: null }) ?: emptyMap()

            //Aqui estoy usando el boolean student para que cuando me salga las tutorias
            //si es false sale por ejemplo Juan Valverde y si es true sale Sergio Velasco
            val editor = pref.edit()
            editor.putBoolean("student", false)
            editor.commit()

            var mentoringModel = ArrayList<DataRoomModel>()

            val ahora = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            //Aqui con el roomId saco el roomName
            for (mentoring in mentoringList) {
                if (mentoring.roomId == null) {
                    mentoringModel.add(DataRoomModel(null, null))
                } else {
                    mentoringModel.add(DataRoomModel(mentoring.roomId, null))
                }
                mentoring.studentAndroid = pref.getBoolean("student", false)
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

            adapter = TutoriasPendientesAdapter(pendientes)
            recyclerView2.adapter = adapter
            adapter2 = TutoriasAsignadasAdapter(otras)
            recyclerView.adapter = adapter2
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()

            adapter2.eliminarTutoriasListener =
                object : TutoriasAsignadasAdapter.EliminarTutoriaListener {
                    override fun eliminarTutoria(item: DataMentoringResponse) {
                        val builder = AlertDialog.Builder(requireContext())
                        val view = layoutInflater.inflate(R.layout.alert, null)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        val cancelar = view.findViewById<Button>(R.id.button)
                        val eliminar = view.findViewById<Button>(R.id.button4)
                        val titulo = view.findViewById<TextView>(R.id.textView7)
                        val mensaje = view.findViewById<TextView>(R.id.textView15)
                        titulo.setText("Eliminar tutoria")
                        mensaje.setText("¿Esta seguro de eliminar esta tutoria?")
                        eliminar.setText("Eliminar")
                        cancelar.setOnClickListener {
                            dialog.dismiss()
                        }
                        eliminar.setOnClickListener {

                            viewModel.patchMentoring(
                                "Bearer $token",
                                item.id!!,
                                null,
                                null,
                                null,
                                "DENIED"
                            )

                            dialog.dismiss()

                        }
                    }
                }

            var fechaStartSeleccionada3 = Calendar.getInstance()

            var fechaEndSeleccionada3 = Calendar.getInstance()

            var elementoSeleccionado3 = ""

            adapter2.modificarTutoriaListener =
                object : TutoriasAsignadasAdapter.ModificarTutoriaListener {
                    override fun modificarTutoria(item: DataMentoringResponse) {
                        val builder = AlertDialog.Builder(requireContext())
                        val view = layoutInflater.inflate(R.layout.alert3, null)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        val formato =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        val fecha = formato.parse(item.start!!)
                        val fecha2 = formato.parse(item.end!!)
                        fechaStartSeleccionada3.time = fecha!!
                        fechaEndSeleccionada3.time = fecha2!!
                        var horaBool = false
                        var hora2Bool = false
                        var diaBool = false
                        var aulaBool = false
                        val button = view.findViewById<Button>(R.id.button5)
                        val modificar = view.findViewById<Button>(R.id.button4)
                        val empieza = view.findViewById<Button>(R.id.button6)
                        val acaba = view.findViewById<Button>(R.id.button7)
                        val spinner = view.findViewById<Spinner>(R.id.spinner)
                        val cancelar = view.findViewById<Button>(R.id.button)
                        val alert = view.findViewById<TextView>(R.id.textView7)
                        alert.setText("Modificar tutoria")
                        modificar.setText("Modificar")
                        button.setText("Dia de la tutoria")

                        // Crea un ArrayAdapter usando el array de strings y el layout predeterminado del Spinner
                        val adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                rooms
                            )

                        // Especifica el layout a usar cuando aparece la lista de opciones
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Aplica el adaptador al Spinner
                        spinner.adapter = adapter

                        val year = fechaStartSeleccionada3.get(Calendar.YEAR)
                        val month = fechaStartSeleccionada3.get(Calendar.MONTH)
                        val day = fechaStartSeleccionada3.get(Calendar.DAY_OF_MONTH)
                        button.setText("$day/${month + 1}/$year")
                        button.setOnClickListener {

                            val dpd = DatePickerDialog(
                                requireContext(),
                                { view, year, monthOfYear, dayOfMonth ->
                                    // Aquí puedes manejar la fecha seleccionada
                                    button.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                                    fechaStartSeleccionada3.set(year, monthOfYear, dayOfMonth)
                                    diaBool = true
                                },
                                year,
                                month,
                                day
                            )

                            dpd.show() // Esto mostrará el DatePickerDialog
                        }

                        cancelar.setOnClickListener {
                            dialog.dismiss()
                        }

                        if (item.roomId != null && roomsId.containsValue(item.roomId)) {
                            val roomName = roomNames[item.roomId]
                            if (roomName != null) {
                                val index = rooms.indexOf(roomName)
                                if (index != -1) {
                                    spinner.setSelection(index)
                                }
                            }
                        }

                        // Define el comportamiento cuando se selecciona un elemento
                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    elementoSeleccionado3 =
                                        parent.getItemAtPosition(position) as String
                                    // Aquí puedes manejar el elemento seleccionado
                                    aulaBool = true
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    // Aquí puedes manejar cuando no se selecciona ningún elemento
                                }
                            }

                        val hour = fechaStartSeleccionada3.get(Calendar.HOUR_OF_DAY)
                        val minute = fechaStartSeleccionada3.get(Calendar.MINUTE)
                        empieza.setText("Empieza: $hour:$minute")

                        empieza.setOnClickListener {

                            val tpd = TimePickerDialog(
                                requireContext(),
                                { view, hourOfDay, minute ->
                                    // Aquí puedes manejar la hora seleccionada
                                    empieza.setText("Empieza: $hourOfDay:$minute")

                                    // Guardamos la hora seleccionada
                                    fechaStartSeleccionada3.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    fechaStartSeleccionada3.set(Calendar.MINUTE, minute)

                                    horaBool = true

                                },
                                hour, minute, true
                            )
                            tpd.show()
                        }

                        val hour2 = fechaEndSeleccionada3.get(Calendar.HOUR_OF_DAY)
                        val minute2 = fechaEndSeleccionada3.get(Calendar.MINUTE)
                        acaba.setText("Acaba: $hour2:$minute2")

                        acaba.setOnClickListener {

                            val tpd = TimePickerDialog(
                                requireContext(),
                                { view, hourOfDay2, minute2 ->
                                    // Aquí puedes manejar la hora seleccionada
                                    acaba.setText("Acaba: $hourOfDay2:$minute2")

                                    // Guardamos la hora seleccionada
                                    fechaEndSeleccionada3.set(Calendar.HOUR_OF_DAY, hourOfDay2)
                                    fechaEndSeleccionada3.set(Calendar.MINUTE, minute2)

                                    hora2Bool = true

                                },
                                hour2, minute2, true
                            )
                            tpd.show()
                        }

                        modificar.setOnClickListener {

                            val formatoDate =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

                            val formatoFecha =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                            val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                            val fechaFormateada =
                                formatoFecha.format(fechaStartSeleccionada3.time)

                            val itemFechaFormateada = formatoDate.parse(item.start)

                            val itemFechaFormateada2 = formatoFecha.format(itemFechaFormateada)

                            val horaStartFormateada =
                                formatoHora.format(fechaStartSeleccionada3.time)

                            val horaEndFormateada =
                                formatoHora.format(fechaEndSeleccionada3.time)

                            val itemHoraStartFormateada = formatoDate.parse(item.start)

                            val itemHoraStartFormateada2 =
                                formatoHora.format(itemHoraStartFormateada)

                            val itemHoraEndFormateada = formatoDate.parse(item.end)

                            val itemHoraEndFormateada2 =
                                formatoHora.format(itemHoraEndFormateada)

                            val aulaId = roomsId[elementoSeleccionado3]

                            var dia: String?
                            var hora: String?
                            var hora2: String?
                            var aula: Int?

                            if (diaBool) {
                                dia = fechaFormateada
                            } else {
                                dia = itemFechaFormateada2
                            }

                            if (horaBool) {
                                hora = horaStartFormateada
                            } else {
                                hora = itemHoraStartFormateada2
                            }

                            if (hora2Bool) {
                                hora2 = horaEndFormateada
                            } else {
                                hora2 = itemHoraEndFormateada2
                            }

                            if (aulaBool) {
                                aula = aulaId
                            } else {
                                aula = item.roomId
                            }

                            val fechaStart = dia + "T" + hora
                            val fechaEnd = dia + "T" + hora2

                            viewModel.patchMentoring(
                                "Bearer $token",
                                item.id!!,
                                fechaStart,
                                fechaEnd,
                                aula,
                                "MODIFIED"
                            )

                            modify = true

                            dialog.dismiss()

                        }

                    }
                }

            adapter.eliminarTutoriaListener =
                object : TutoriasPendientesAdapter.EliminarTutoriaListener {
                    override fun eliminarTutoria(item: DataMentoringResponse) {
                        val builder = AlertDialog.Builder(requireContext())
                        val view = layoutInflater.inflate(R.layout.alert, null)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        val cancelar = view.findViewById<Button>(R.id.button)
                        val eliminar = view.findViewById<Button>(R.id.button4)
                        val titulo = view.findViewById<TextView>(R.id.textView7)
                        val mensaje = view.findViewById<TextView>(R.id.textView15)
                        titulo.setText("Eliminar tutoria")
                        mensaje.setText("¿Esta seguro de eliminar esta tutoria?")
                        eliminar.setText("Eliminar")
                        cancelar.setOnClickListener {
                            dialog.dismiss()
                        }
                        eliminar.setOnClickListener {

                            viewModel.patchMentoring(
                                "Bearer $token",
                                item.id!!,
                                null,
                                null,
                                null,
                                "DENIED"
                            )

                            dialog.dismiss()

                        }
                    }
                }
            var fechaStartSeleccionada = Calendar.getInstance()

            var fechaEndSeleccionada = Calendar.getInstance()

            var elementoSeleccionado = ""

            adapter.modificarTutoriaListener =
                object : TutoriasPendientesAdapter.ModificarTutoriaListener {
                    override fun modificarTutoria(item: DataMentoringResponse) {
                        val builder = AlertDialog.Builder(requireContext())
                        val view = layoutInflater.inflate(R.layout.alert3, null)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        val formato =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        val fecha = formato.parse(item.start!!)
                        val fecha2 = formato.parse(item.end!!)
                        fechaStartSeleccionada.time = fecha!!
                        fechaEndSeleccionada.time = fecha2!!
                        var horaBool = false
                        var hora2Bool = false
                        var diaBool = false
                        var aulaBool = false
                        val button = view.findViewById<Button>(R.id.button5)
                        val modificar = view.findViewById<Button>(R.id.button4)
                        val empieza = view.findViewById<Button>(R.id.button6)
                        val acaba = view.findViewById<Button>(R.id.button7)
                        val spinner = view.findViewById<Spinner>(R.id.spinner)
                        val cancelar = view.findViewById<Button>(R.id.button)
                        val alert = view.findViewById<TextView>(R.id.textView7)
                        alert.setText("Modificar tutoria")
                        modificar.setText("Modificar")
                        button.setText("Dia de la tutoria")

                        // Crea un ArrayAdapter usando el array de strings y el layout predeterminado del Spinner
                        val adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                rooms
                            )

                        // Especifica el layout a usar cuando aparece la lista de opciones
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Aplica el adaptador al Spinner
                        spinner.adapter = adapter
                        val year = fechaStartSeleccionada.get(Calendar.YEAR)
                        val month = fechaStartSeleccionada.get(Calendar.MONTH)
                        val day = fechaStartSeleccionada.get(Calendar.DAY_OF_MONTH)
                        button.setText("$day/${month + 1}/$year")
                        button.setOnClickListener {

                            val dpd = DatePickerDialog(
                                requireContext(),
                                { view, year, monthOfYear, dayOfMonth ->
                                    // Aquí puedes manejar la fecha seleccionada
                                    button.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                                    fechaStartSeleccionada.set(year, monthOfYear, dayOfMonth)
                                    diaBool = true
                                },
                                year,
                                month,
                                day
                            )

                            dpd.show() // Esto mostrará el DatePickerDialog
                        }

                        cancelar.setOnClickListener {
                            dialog.dismiss()
                        }

                        //spinner.setSelection(item.roomId!! - 1)

                        // Define el comportamiento cuando se selecciona un elemento
                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    elementoSeleccionado =
                                        parent.getItemAtPosition(position) as String
                                    // Aquí puedes manejar el elemento seleccionado
                                    aulaBool = true
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    // Aquí puedes manejar cuando no se selecciona ningún elemento
                                }
                            }

                        val hour = fechaStartSeleccionada.get(Calendar.HOUR_OF_DAY)
                        val minute = fechaStartSeleccionada.get(Calendar.MINUTE)
                        //empieza.setText("Empieza: $hour:$minute")

                        empieza.setOnClickListener {

                            val tpd = TimePickerDialog(
                                requireContext(),
                                { view, hourOfDay, minute ->
                                    // Aquí puedes manejar la hora seleccionada
                                    empieza.setText("Empieza: $hourOfDay:$minute")

                                    // Guardamos la hora seleccionada
                                    fechaStartSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    fechaStartSeleccionada.set(Calendar.MINUTE, minute)

                                    horaBool = true

                                },
                                hour, minute, true
                            )
                            tpd.show()
                        }

                        val hour2 = fechaEndSeleccionada.get(Calendar.HOUR_OF_DAY)
                        val minute2 = fechaEndSeleccionada.get(Calendar.MINUTE)
                        //acaba.setText("Acaba: $hour2:$minute2")

                        acaba.setOnClickListener {

                            val tpd = TimePickerDialog(
                                requireContext(),
                                { view, hourOfDay2, minute2 ->
                                    // Aquí puedes manejar la hora seleccionada
                                    acaba.setText("Acaba: $hourOfDay2:$minute2")

                                    // Guardamos la hora seleccionada
                                    fechaEndSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay2)
                                    fechaEndSeleccionada.set(Calendar.MINUTE, minute2)

                                    hora2Bool = true

                                },
                                hour2, minute2, true
                            )
                            tpd.show()
                        }

                        modificar.setOnClickListener {

                            val formatoDate =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

                            val formatoFecha =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                            val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                            val fechaFormateada =
                                formatoFecha.format(fechaStartSeleccionada.time)

                            val itemFechaFormateada = formatoDate.parse(item.start)

                            val itemFechaFormateada2 = formatoFecha.format(itemFechaFormateada)

                            val horaStartFormateada =
                                formatoHora.format(fechaStartSeleccionada.time)

                            val horaEndFormateada =
                                formatoHora.format(fechaEndSeleccionada.time)

                            val itemHoraStartFormateada = formatoDate.parse(item.start)

                            val itemHoraStartFormateada2 =
                                formatoHora.format(itemHoraStartFormateada)

                            val itemHoraEndFormateada = formatoDate.parse(item.end)

                            val itemHoraEndFormateada2 =
                                formatoHora.format(itemHoraEndFormateada)

                            val aulaId = roomsId[elementoSeleccionado]

                            var dia: String?
                            var hora: String?
                            var hora2: String?
                            var aula: Int?

                            if (diaBool) {
                                dia = fechaFormateada
                            } else {
                                dia = itemFechaFormateada2
                            }

                            if (horaBool) {
                                hora = horaStartFormateada
                            } else {
                                hora = itemHoraStartFormateada2
                            }

                            if (hora2Bool) {
                                hora2 = horaEndFormateada
                            } else {
                                hora2 = itemHoraEndFormateada2
                            }

                            if (aulaBool) {
                                aula = aulaId
                            } else {
                                aula = item.roomId
                            }

                            val fechaStart = dia + "T" + hora
                            val fechaEnd = dia + "T" + hora2

                            viewModel.patchMentoring(
                                "Bearer $token",
                                item.id!!,
                                fechaStart,
                                fechaEnd,
                                aula,
                                "MODIFIED"
                            )

                            modify = true

                            dialog.dismiss()

                        }

                    }
                }

            var fechaStartSeleccionada2 = Calendar.getInstance()

            var fechaEndSeleccionada2 = Calendar.getInstance()

            var elementoSeleccionado2 = ""

            var elementoSeleccionado2Start = ""

            var elementoSeleccionado2End = ""

            adapter.asignarTutoriaListener =
                object : TutoriasPendientesAdapter.AsignarTutoriaListener {
                    override fun asignarTutoria(item: DataMentoringResponse) {
                        val builder = AlertDialog.Builder(requireContext())
                        val view = layoutInflater.inflate(R.layout.alert2, null)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        val formato =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        val formatoHours =
                            SimpleDateFormat("HH:mm", Locale.getDefault())
                        val fecha = formato.parse(item.start!!)
                        val fecha2 = formato.parse(item.end!!)
                        fechaStartSeleccionada2.time = fecha!!
                        fechaEndSeleccionada2.time = fecha2!!
                        var horaStart = false
                        var horaEnd = false
                        val asignar = view.findViewById<Button>(R.id.button4)
                        asignar.setText("Asignar")
                        val alert = view.findViewById<TextView>(R.id.textView7)
                        alert.setText("Asignar tutoria")
                        val empieza = view.findViewById<Button>(R.id.button6)
                        empieza.setText("Hora comienzo")
                        val acaba = view.findViewById<Button>(R.id.button8)
                        acaba.setText("Hora acaba")
                        val spinner = view.findViewById<Spinner>(R.id.spinner)
                        //val spinner2 = view.findViewById<Spinner>(R.id.spinner2)
                        //val spinner3 = view.findViewById<Spinner>(R.id.spinner3)
                        val cancelar = view.findViewById<Button>(R.id.button)
                        // Crea un ArrayAdapter usando el array de strings y el layout predeterminado del Spinner
                        val adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                rooms
                            )

                        /*val adapter2 =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                hours
                            )*/

                        // Especifica el layout a usar cuando aparece la lista de opciones
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Aplica el adaptador al Spinner
                        spinner.adapter = adapter

                        /*spinner2.adapter = adapter2

                        spinner3.adapter = adapter2*/

                        cancelar.setOnClickListener {
                            dialog.dismiss()
                        }

                        empieza.setOnClickListener {
                            val tpd = TimePickerDialog(
                                requireContext(),
                                { view, hourOfDay, minute ->
                                    // Aqui puedes manejar la hora seleccionada
                                    empieza.setText("Empieza: $hourOfDay:$minute")

                                    // Guardamos la hora seleccionada
                                    fechaStartSeleccionada2.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    fechaStartSeleccionada2.set(Calendar.MINUTE, minute)

                                    horaStart = true
                                },
                                0, 0, true
                            )
                            tpd.show()
                        }

                        acaba.setOnClickListener {
                            val tpd = TimePickerDialog(
                                requireContext(),
                                { view, hourOfDay, minute ->
                                    // Aqui puedes manejar la hora seleccionada
                                    acaba.setText("Acaba: $hourOfDay:$minute")

                                    // Guardamos la hora seleccionada
                                    fechaEndSeleccionada2.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    fechaEndSeleccionada2.set(Calendar.MINUTE, minute)

                                    horaEnd = true
                                },
                                0, 0, true
                            )
                            tpd.show()
                        }

                        /*val specificHoursStart = formatoHours.format(fecha)

                        val specificHoursEnd = formatoHours.format(fecha2)

                        val index = hours.indexOf(specificHoursStart)

                        val index2 = hours.indexOf(specificHoursEnd)

                        if (index != -1) {
                            spinner2.setSelection(index)
                        }

                        if (index2 != -1) {
                            spinner3.setSelection(index2)
                        }*/

                        /*spinner2.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    elementoSeleccionado2Start =
                                        parent!!.getItemAtPosition(position) as String
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Aquí puedes manejar cuando no se selecciona ningún elemento
                                }
                            }*/

                        /*spinner3.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    elementoSeleccionado2End =
                                        parent!!.getItemAtPosition(position) as String
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Aquí puedes manejar cuando no se selecciona ningún elemento
                                }
                            }*/

                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    elementoSeleccionado2 =
                                        parent!!.getItemAtPosition(position) as String
                                    // Aquí puedes manejar el elemento seleccionado
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Aquí puedes manejar cuando no se selecciona ningún elemento
                                }

                            }

                        asignar.setOnClickListener {

                            val formatoDate =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

                            val formatoFecha =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                            val formatoHora = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                            val fechaFormateada =
                                formatoFecha.format(fechaStartSeleccionada2.time)

                            val itemFechaFormateada = formatoDate.parse(item.start)

                            val itemFechaFormateada2 = formatoFecha.format(itemFechaFormateada)

                            val horaStartFormateada =
                                formatoHora.format(fechaStartSeleccionada2.time)

                            val horaEndFormateada =
                                formatoHora.format(fechaEndSeleccionada2.time)

                            val aulaId = roomsId[elementoSeleccionado2]

                            if (horaStart && horaEnd) {

                                val fechaStart =
                                    itemFechaFormateada2 + "T" + horaStartFormateada
                                val fechaEnd = itemFechaFormateada2 + "T" + horaEndFormateada

                                viewModel.patchMentoring(
                                    "Bearer $token",
                                    item.id!!,
                                    fechaStart,
                                    fechaEnd,
                                    aulaId,
                                    "APPROVED"
                                )

                                asign = true

                                dialog.dismiss()

                            } else {
                                val builder2 = AlertDialog.Builder(requireContext())
                                val view2 =
                                    layoutInflater.inflate(R.layout.alert_response, null)
                                val textview = view2.findViewById<TextView>(R.id.textView16)
                                textview.setText("Error tienes que elegir la hora de comenzar y acabar")
                                textview.setBackgroundResource(R.drawable.rounded_textview_error)
                                builder2.setView(view2)
                                val dialog2 = builder2.create()
                                dialog2.show()
                                dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                Handler(Looper.getMainLooper()).postDelayed({
                                    if (dialog2.isShowing) {
                                        dialog2.dismiss()
                                    }
                                }, 5000)
                            }
                        }
                    }
                }
        })

        viewModel.liveDataRoomList.observe(viewLifecycleOwner, Observer
        {
            val roomMentoring = it.filter { room -> room.mentoringRoom }
            rooms = roomMentoring.map { it.name }.toTypedArray()
            roomsId = roomMentoring.associate { room -> room.name to room.id }

            var index = 0

            for (room in roomMentoring) {

                for (timeTable in room.timeTables) {

                    val start = timeTable.start

                    val end = timeTable.end

                    occupededHours[index] = Pair(start, end)

                    index++

                }
            }

        })

        viewModel.liveDataMentoring.observe(viewLifecycleOwner, Observer
        {
            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            val textview = view2.findViewById<TextView>(R.id.textView16)
            if (asign) {
                textview.setText("Tutoria asignada con exito")
            } else if (modify) {
                textview.setText("Tutoria modificada con exito")
            } else {
                textview.setText("Tutoria eliminada con exito!")
            }
            builder2.setView(view2)
            val dialog2 = builder2.create()
            viewModel.getRoomList("Bearer $token")
            viewModel.getMentoringTeacher("Bearer $token", id)
            dialog2.show()
            dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog2.isShowing) {

                    dialog2.dismiss()
                }
            }, 5000)
            asign = false
            modify = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}