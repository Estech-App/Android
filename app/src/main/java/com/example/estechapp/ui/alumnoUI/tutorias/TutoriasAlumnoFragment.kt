package com.example.estechapp.ui.alumnoUI.tutorias

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.R
import com.example.estechapp.data.models.DataMentoringResponse
import com.example.estechapp.data.models.DataRoomModel
import com.example.estechapp.databinding.FragmentTutoriasAlumnoBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapterAlumno
import com.example.estechapp.ui.adapter.TutoriasPendientesAdapterAlumno
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TutoriasAlumnoFragment : Fragment() {

    private var _binding: FragmentTutoriasAlumnoBinding? = null

    private lateinit var adapter: TutoriasPendientesAdapterAlumno

    private lateinit var adapter2: TutoriasAsignadasAdapterAlumno

    private lateinit var rooms: Array<String>

    private lateinit var roomsId: Map<String, Int>

    private lateinit var roomNames: Map<Int, String?>

    private lateinit var profesores: Array<String>

    private lateinit var profesoresId: Map<String, Int>

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTutoriasAlumnoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recibo los datos.
        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        //Voy preparando el recyclerview.
        val recyclerView = binding.recyclerTutoriasAlumno
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        val recyclerView2 = binding.recyclerPendientes
        val llm2 = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = llm2

        var asignar = false

        viewModel.getRoomList("Bearer $token")

        viewModel.getMentoringStudent("Bearer $token", id)

        viewModel.getUserByRole("Bearer $token", 3)

        binding.imageView5.setOnClickListener {
            var elementoSeleccionado = ""
            var fechaSeleccionada = Calendar.getInstance()
            var diaBool = false
            val year = fechaSeleccionada.get(Calendar.YEAR)
            val month = fechaSeleccionada.get(Calendar.MONTH)
            val day = fechaSeleccionada.get(Calendar.DAY_OF_MONTH)
            val builder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.alert4, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val cancelar = view.findViewById<Button>(R.id.button)
            val solicitar = view.findViewById<Button>(R.id.button4)
            val fecha = view.findViewById<Button>(R.id.button5)
            val alert = view.findViewById<TextView>(R.id.textView7)
            val spinner = view.findViewById<Spinner>(R.id.spinner)
            alert.setText("Solicitar tutoria")
            fecha.setText("Dia de la tutoria")
            solicitar.setText("Solicitar")
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                profesores
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = adapter

            cancelar.setOnClickListener {
                dialog.dismiss()
            }

            spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        elementoSeleccionado = parent!!.getItemAtPosition(position) as String
                        // Aquí puedes manejar el elemento seleccionado
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Aquí puedes manejar cuando no se selecciona ningún elemento
                    }

                }

            fecha.setOnClickListener {

                val dpd = DatePickerDialog(
                    requireContext(),
                    { view, year, monthOfYear, dayOfMonth ->
                        fecha.setText("$dayOfMonth/${monthOfYear + 1}/$year")
                        fechaSeleccionada.set(year, monthOfYear, dayOfMonth)
                        diaBool = true
                    },
                    year,
                    month,
                    day
                )

                dpd.show()

            }

            solicitar.setOnClickListener {

                fechaSeleccionada.set(Calendar.HOUR_OF_DAY, 0)
                fechaSeleccionada.set(Calendar.MINUTE, 0)
                fechaSeleccionada.set(Calendar.SECOND, 0)

                val fechaSeleccionadaFormateada = formato.format(fechaSeleccionada.time)

                val profesorId = profesoresId[elementoSeleccionado]

                if (diaBool) {
                    viewModel.postMentoring(
                        "Bearer $token",
                        fechaSeleccionadaFormateada,
                        fechaSeleccionadaFormateada,
                        null,
                        "PENDING",
                        profesorId!!,
                        id
                    )

                    asignar = true

                    dialog.dismiss()

                } else {
                    val builder2 = AlertDialog.Builder(requireContext())
                    val view2 = layoutInflater.inflate(R.layout.alert_response, null)
                    val textview = view2.findViewById<TextView>(R.id.textView16)
                    textview.setText("Error tienes que elegir la fecha de la tutoria")
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

        viewModel.liveDataRoomAndMentoring.observe(viewLifecycleOwner, Observer { pair ->
            val roomList = pair.first
            val mentoringList = pair.second

            roomNames =
                roomList?.associateBy({ it.id }, { it.id?.run { it.name } ?: null }) ?: emptyMap()

            //Esto es para que cuando seas alumno ver el nombre del profesor en la tutoria.
            val editor = pref.edit()
            editor.putBoolean("student", true)
            editor.commit()

            var mentoringModel = ArrayList<DataRoomModel>()

            val ahora = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            for (mentoring in mentoringList) {
                if (mentoring.roomId == null) {
                    mentoringModel.add(DataRoomModel(null, null))
                } else {
                    mentoringModel.add(DataRoomModel(mentoring.roomId, null))
                }
                mentoring.studentAndroid = pref.getBoolean("student", true)
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

            adapter = TutoriasPendientesAdapterAlumno(pendientes)
            recyclerView2.adapter = adapter
            adapter2 = TutoriasAsignadasAdapterAlumno(otras)
            recyclerView.adapter = adapter2
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()

            adapter2.eliminarTutoriasListener =
                object : TutoriasAsignadasAdapterAlumno.EliminarTutoriaListener {
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

            adapter.eliminarTutoriaListener =
                object : TutoriasPendientesAdapterAlumno.EliminarTutoriaListener {
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
        })

        viewModel.liveDataUserRoleList.observe(viewLifecycleOwner, Observer {
            profesores = it.mapNotNull { it.name }.toTypedArray()
            profesoresId = it.associate { profesor -> profesor.name to profesor.id } as Map<String, Int>
        })

        viewModel.liveDataRoomList.observe(viewLifecycleOwner, Observer {
            val roomMentoring = it.filter { room -> room.mentoringRoom }
            rooms = roomMentoring.map { it.name }.toTypedArray()
            roomsId = roomMentoring.associate { room -> room.name to room.id }
        })

        viewModel.liveDataMentoring.observe(viewLifecycleOwner, Observer {
            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            val textview = view2.findViewById<TextView>(R.id.textView16)
            if (asignar) {
                textview.setText("Tutoria solicitada con exito")
            } else {
                textview.setText("Tutoria eliminada con exito!")
            }
            builder2.setView(view2)
            val dialog2 = builder2.create()
            viewModel.getRoomList("Bearer $token")
            viewModel.getMentoringStudent("Bearer $token", id)
            dialog2.show()
            dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog2.isShowing) {

                    dialog2.dismiss()
                }
            }, 5000)
            asignar = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}