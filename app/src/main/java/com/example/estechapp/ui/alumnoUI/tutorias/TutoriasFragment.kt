package com.example.estechapp.ui.alumnoUI.tutorias

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.estechapp.data.models.Tutoria
import com.example.estechapp.databinding.FragmentTutoriasAlumnoBinding
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapter

class TutoriasFragment : Fragment() {

    private var _binding: FragmentTutoriasAlumnoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTutoriasAlumnoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerTutoriasAlumno.adapter = TutoriasAsignadasAdapter(
            listOf(
                Tutoria("Ramon", "DAM 2º", "Aula DAM","","","",true)
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}