package com.example.estechapp.ui.profesorUI.tutorias

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.databinding.FragmentTutoriasBinding
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapter
import com.example.estechapp.ui.adapter.TutoriasPendientesAdapter
import com.example.estechapp.ui.MyViewModel

class TutoriasFragment : Fragment() {

    private var _binding: FragmentTutoriasBinding? = null

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

        viewModel.getMentoringTeacher("Bearer $token", id)

        /*val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.alert, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val cancelar = view.findViewById<Button>(R.id.button)
        val enviar = view.findViewById<Button>(R.id.button4)
        val titulo = view.findViewById<TextView>(R.id.textView7)
        val mensaje = view.findViewById<TextView>(R.id.textView15)
        titulo.setText("Eliminar tutoria")
        mensaje.setText("¿Seguro que desea eliminar esta tutoria?")
        enviar.setText("Confirmar")
        cancelar.setOnClickListener {
            dialog.dismiss()
        }
        enviar.setOnClickListener {

            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            val mensaje = view2.findViewById<TextView>(R.id.textView16)
            mensaje.setText("Tutoria eliminada con exito!")
            builder2.setView(view2)
            val dialog2 = builder2.create()
            dialog2.show()
            dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog2.isShowing) {
                    dialog2.dismiss()
                }
            }, 5000)
            dialog.dismiss()
        }*/

        viewModel.liveDataMentoring.observe(viewLifecycleOwner, Observer { mentoringList ->
            if (mentoringList != null) {

                //Aqui estoy usando el boolean student para que cuando me salga las tutorias
                //si es false sale por ejemplo Juan Valverde y si es true sale Sergio Velasco
                val editor = pref.edit()
                editor.putBoolean("student", false)
                editor.commit()

                //Aqui con el roomId saco el roomName
                for (mentoring in mentoringList) {
                    viewModel.getRoomId("Bearer $token", mentoring.roomId)
                    mentoring.roomName = pref.getString("room", "")!!
                    mentoring.studentAndroid = pref.getBoolean("student", false)
                }
                //Aqui si es pending se va a PendientesAdapter
                //Y si es approved o modified se van a AsignadasAdapter
                val pendientes = mentoringList.filter { it.status == "PENDING" }
                val otras = mentoringList.filter { it.status == "APPROVED" || it.status == "MODIFIED" }

                recyclerView.adapter = TutoriasAsignadasAdapter(otras)
                recyclerView2.adapter = TutoriasPendientesAdapter(pendientes)
            }
        })

        //Aqui con el roomId saco el nombre.
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