package com.example.estechapp.ui.profesorUI.tutorias

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.estechapp.databinding.FragmentTutoriasBinding
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapter
import com.example.estechapp.ui.adapter.TutoriasPendientesAdapter
import com.example.estechapp.data.models.Tutoria

class TutoriasFragment : Fragment() {

    private var _binding: FragmentTutoriasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        binding.recyclerAsignadas.adapter = TutoriasAsignadasAdapter(
            listOf(
                Tutoria("Ramon", "DAM 2º", "Aula DAM","","","",true)
            )
        )

        binding.recyclerPendientes.adapter = TutoriasPendientesAdapter(
            mutableListOf(
                Tutoria("Ramon", "DAM 2º", "Aula DAM","","","",true)
            )

        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}