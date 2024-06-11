package com.example.estechapp.ui.alumnoUI.practica

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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.R
import com.example.estechapp.data.models.DataFreeUsageResponse
import com.example.estechapp.data.models.DataRoomModel
import com.example.estechapp.databinding.FragmentPracticaBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.PracticasLibresAsignadasAdapter
import com.example.estechapp.ui.adapter.PracticasLibresPendientesAdapter
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapter
import com.example.estechapp.ui.adapter.TutoriasPendientesAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PracticaFragment : Fragment() {

    private var _binding: FragmentPracticaBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: PracticasLibresPendientesAdapter

    private lateinit var adapter2: PracticasLibresAsignadasAdapter

    private lateinit var rooms: Array<String>

    private lateinit var roomsId: Map<String, Int>

    private lateinit var roomNames: Map<Int, String?>

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPracticaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        val recyclerView = binding.recyclerPracticas
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        val recyclerView2 = binding.recyclerPracticasPendientes
        val llm2 = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = llm2

        var asign = false

        viewModel.getRoomList("Bearer $token")

        viewModel.getFreeUsage("Bearer $token", id)

        binding.imageView5.setOnClickListener {
            var elementoSeleccionado = ""
            var fechaStartSeleccionada = Calendar.getInstance()
            var fechaEndSeleccionada = Calendar.getInstance()
            var diaBool = false
            var horaBool = false
            var hora2Bool = false
            var aulaBool = false
            val year = fechaStartSeleccionada.get(Calendar.YEAR)
            val month = fechaStartSeleccionada.get(Calendar.MONTH)
            val day = fechaStartSeleccionada.get(Calendar.DAY_OF_MONTH)
            val builder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.alert3, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val cancelar = view.findViewById<Button>(R.id.button)
            val solicitar = view.findViewById<Button>(R.id.button4)
            val fecha = view.findViewById<Button>(R.id.button5)
            val alert = view.findViewById<TextView>(R.id.textView7)
            val empieza = view.findViewById<Button>(R.id.button6)
            val acaba = view.findViewById<Button>(R.id.button7)
            val spinner = view.findViewById<Spinner>(R.id.spinner)
            fecha.setText("Dia de la practica libre")
            alert.setText("Solicitar practica libre")
            solicitar.setText("Solicitar")
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                rooms
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner.adapter = adapter

            cancelar.setOnClickListener {
                dialog.dismiss()
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    elementoSeleccionado = parent!!.getItemAtPosition(position) as String
                    aulaBool = true
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
                        fechaStartSeleccionada.set(year, monthOfYear, dayOfMonth)
                        fechaEndSeleccionada.set(year, monthOfYear, dayOfMonth)
                        diaBool = true
                    },
                    year,
                    month,
                    day
                )

                dpd.show()

            }

            empieza.setOnClickListener {
                val tpd = TimePickerDialog(
                    requireContext(),
                    { view, hourOfDay, minute ->
                        fechaStartSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        fechaStartSeleccionada.set(Calendar.MINUTE, minute)
                        fechaStartSeleccionada.set(Calendar.SECOND, 0)
                        empieza.setText("Empieza: $hourOfDay:$minute")
                        horaBool = true
                    },
                    24, 0, true
                )
                tpd.show()
            }

            acaba.setOnClickListener {
                val tpd2 = TimePickerDialog(
                    requireContext(),
                    { view, hourOfDay2, minute2 ->
                        fechaEndSeleccionada.set(Calendar.HOUR_OF_DAY, hourOfDay2)
                        fechaEndSeleccionada.set(Calendar.MINUTE, minute2)
                        fechaEndSeleccionada.set(Calendar.SECOND, 0)
                        acaba.setText("Acaba: $hourOfDay2:$minute2")
                        hora2Bool = true
                    },
                    24, 0, true
                )
                tpd2.show()
            }

            solicitar.setOnClickListener {

                var aula: Int

                if (aulaBool && diaBool && horaBool && hora2Bool) {
                    aula = roomsId[elementoSeleccionado]!!
                    var fechaStart = formato.format(fechaStartSeleccionada.time)
                    var fechaEnd = formato.format(fechaEndSeleccionada.time)

                    viewModel.postFreeUsage(
                        "Bearer $token",
                        fechaStart,
                        fechaEnd,
                        "PENDING",
                        aula,
                        id
                    )

                    asign = true

                    dialog.dismiss()

                } else {
                    val builder2 = android.app.AlertDialog.Builder(requireContext())
                    val view2 =
                        layoutInflater.inflate(R.layout.alert_response, null)
                    val textview = view2.findViewById<TextView>(R.id.textView16)
                    textview.setText("Error tienes que elegir la hora de comenzar y acabar y la fecha")
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

        viewModel.liveDataRoomAndFreeUsage.observe(viewLifecycleOwner, Observer { pair ->
            val roomList = pair.first
            val freeUsageList = pair.second

            roomNames =
                roomList?.associateBy({ it.id }, { it.id?.run { it.name } ?: null }) ?: emptyMap()

            var freeUsageModel = ArrayList<DataRoomModel>()

            val ahora = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            for (freeUsage in freeUsageList) {
                if (freeUsage.room.id == null) {
                    freeUsageModel.add(DataRoomModel(null, null))
                } else {
                    freeUsageModel.add(DataRoomModel(freeUsage.room.id, null))
                }
            }

            for (freeUsage in freeUsageModel) {
                if (freeUsage.roomId == null) {
                    freeUsage.roomName = null
                } else {
                    val roomName = roomNames[freeUsage.roomId]
                    if (roomName != null) {
                        freeUsage.roomName = roomName
                    }
                }
            }

            for (i in freeUsageList.indices) {
                if (freeUsageModel[i].roomName == null) {
                    freeUsageList[i].room.name = null.toString()
                } else {
                    freeUsageList[i].room.name = freeUsageModel[i].roomName!!
                }
            }

            //Aqui si es pending se va a PendientesAdapter
            //Y si es approved o modified se van a AsignadasAdapter
            val pendientes = freeUsageList.filter {
                it.status == "PENDING" && (formato.parse(it.start)
                    .equals(ahora.time) || formato.parse(it.start).after(ahora.time))
            }
            val otras = freeUsageList.filter {
                (it.status == "APPROVED" || it.status == "MODIFIED") && (formato.parse(it.start)
                    .equals(ahora.time) || formato.parse(it.start).after(ahora.time))
            }

            adapter = PracticasLibresPendientesAdapter(pendientes)
            recyclerView2.adapter = adapter
            adapter2 = PracticasLibresAsignadasAdapter(otras)
            recyclerView.adapter = adapter2
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()

            adapter2.eliminarPracticaLibreListener =
                object : PracticasLibresAsignadasAdapter.EliminarPracticaLibreListener {
                    override fun eliminarPracticaLibre(item: DataFreeUsageResponse) {
                        val builder = android.app.AlertDialog.Builder(requireContext())
                        val view = layoutInflater.inflate(R.layout.alert, null)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        val cancelar = view.findViewById<Button>(R.id.button)
                        val eliminar = view.findViewById<Button>(R.id.button4)
                        val titulo = view.findViewById<TextView>(R.id.textView7)
                        val mensaje = view.findViewById<TextView>(R.id.textView15)
                        titulo.setText("Eliminar practica libre")
                        mensaje.setText("¿Esta seguro de eliminar esta practica libre?")
                        eliminar.setText("Eliminar")
                        cancelar.setOnClickListener {
                            dialog.dismiss()
                        }
                        eliminar.setOnClickListener {
                            viewModel.patchFreeUsage(
                                "Bearer $token",
                                item.id!!,
                                "DENIED"
                            )

                            dialog.dismiss()
                        }
                    }
                }

            adapter.eliminarPracticaLibreListener =
                object : PracticasLibresPendientesAdapter.EliminarPracticaLibreListener {
                    override fun eliminarPracticaLibre(item: DataFreeUsageResponse) {
                        val builder = android.app.AlertDialog.Builder(requireContext())
                        val view = layoutInflater.inflate(R.layout.alert, null)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        val cancelar = view.findViewById<Button>(R.id.button)
                        val eliminar = view.findViewById<Button>(R.id.button4)
                        val titulo = view.findViewById<TextView>(R.id.textView7)
                        val mensaje = view.findViewById<TextView>(R.id.textView15)
                        titulo.setText("Eliminar practica libre")
                        mensaje.setText("¿Esta seguro de eliminar esta practica libre?")
                        eliminar.setText("Eliminar")
                        cancelar.setOnClickListener {
                            dialog.dismiss()
                        }
                        eliminar.setOnClickListener {
                            viewModel.patchFreeUsage(
                                "Bearer $token",
                                item.id!!,
                                "DENIED"
                            )

                            dialog.dismiss()
                        }
                    }
                }

        })

        viewModel.liveDataRoomList.observe(viewLifecycleOwner, Observer
        {
            val roomStudy = it.filter { room -> room.studyRoom }
            rooms = roomStudy.map { it.name }.toTypedArray()
            roomsId = roomStudy.associate { room -> room.name to room.id }
        })

        viewModel.liveDataFreeUsage.observe(viewLifecycleOwner, Observer
        {
            val builder2 = android.app.AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            val textview = view2.findViewById<TextView>(R.id.textView16)
            if (asign) {
                textview.setText("Practica Libre asignada con exito")
            } else {
                textview.setText("Practica Libre eliminada con exito!")
            }
            builder2.setView(view2)
            val dialog2 = builder2.create()
            viewModel.getRoomList("Bearer $token")
            viewModel.getFreeUsage("Bearer $token", id)
            dialog2.show()
            dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog2.isShowing) {

                    dialog2.dismiss()
                }
            }, 5000)
            asign = false
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}