package com.example.estechapp.ui.alumnoUI.tutorias

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

    private lateinit var roomNames: Map<Int, String>

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

        var liveDataRoomCompleted = false

        viewModel.getRoomList("Bearer $token")

        viewModel.getMentoringStudent("Bearer $token", id)

        binding.imageView5.setOnClickListener {

        }

        viewModel.liveDataRoomList.observe(viewLifecycleOwner, Observer {
            roomNames = it.associateBy({ it.id }, { it.name })
            liveDataRoomCompleted = true
        })

        //Esto es para recibir todas las tutorias por id.
        viewModel.liveDataMentoringList.observe(viewLifecycleOwner, Observer { it ->
            if (it != null) {

                while (!liveDataRoomCompleted) {

                }

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

                for (mentoring in it) {
                    if (mentoring.roomId == null) {
                        mentoringModel.add(DataRoomModel(null, null))
                    } else {
                        mentoringModel.add(DataRoomModel(mentoring.roomId, null))
                        mentoring.studentAndroid = pref.getBoolean("student", true)
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

                for (i in it.indices) {
                    if (mentoringModel[i].roomName == null) {
                        it[i].roomName = null
                    } else {
                        it[i].roomName = mentoringModel[i].roomName
                    }
                }

                //Aqui si es pending se va a PendientesAdapter
                //Y si es approved o modified se van a AsignadasAdapter
                val pendientes = it.filter {
                    it.status == "PENDING" && (formato.parse(it.start)
                        .equals(ahora.time) || formato.parse(it.start).after(ahora.time))
                }
                val otras = it.filter {
                    (it.status == "APPROVED" || it.status == "MODIFIED") && (formato.parse(it.start)
                        .equals(ahora.time) || formato.parse(it.start).after(ahora.time))
                }

                adapter = TutoriasPendientesAdapterAlumno(pendientes)
                recyclerView2.adapter = adapter
                adapter2 = TutoriasAsignadasAdapterAlumno(otras)
                recyclerView.adapter = adapter2

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

                                viewModel.getMentoringTeacher("Bearer $token", id)
                                adapter.notifyDataSetChanged()

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

                                viewModel.getMentoringTeacher("Bearer $token", id)
                                adapter.notifyDataSetChanged()

                            }
                        }
                    }

            }
        })

        viewModel.liveDataRoomList.observe(viewLifecycleOwner, Observer {
            rooms = it.map { it.name }.toTypedArray()
            roomsId = it.associate { room -> room.name to room.id }
        })

        viewModel.liveDataMentoring.observe(viewLifecycleOwner, Observer {
            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            val textview = view2.findViewById<TextView>(R.id.textView16)
            textview.setText("Tutoria eliminada con exito!")
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