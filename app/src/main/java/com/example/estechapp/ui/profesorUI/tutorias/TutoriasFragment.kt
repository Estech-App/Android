package com.example.estechapp.ui.profesorUI.tutorias

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
import com.example.estechapp.databinding.FragmentTutoriasBinding
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapter
import com.example.estechapp.ui.adapter.TutoriasPendientesAdapter
import com.example.estechapp.data.models.Tutoria
import com.example.estechapp.ui.MyViewModel

class TutoriasFragment : Fragment() {

    private var _binding: FragmentTutoriasBinding? = null

    private val viewModel by viewModels<MyViewModel> {
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

        _binding = FragmentTutoriasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

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
                val pendientes = mentoringList.filter { it.status == "PENDING" }
                val otras = mentoringList.filter { it.status != "PENDING" }

                // Aquí puedes usar 'pendientes' y 'otras' para actualizar tus RecyclerViews
                // Por ejemplo:
                recyclerView.adapter = TutoriasAsignadasAdapter(otras)
                recyclerView2.adapter = TutoriasPendientesAdapter(pendientes)
            }
        })

    }
        /*binding.recyclerAsignadas.adapter = TutoriasAsignadasAdapter(
            listOf(
                Tutoria("Ramon", "DAM 2º", "Aula DAM","","","",true)
            )
        )

        binding.recyclerPendientes.adapter = TutoriasPendientesAdapter(
            mutableListOf(
                Tutoria("Ramon", "DAM 2º", "Aula DAM","","","",true)
            )

        )
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}