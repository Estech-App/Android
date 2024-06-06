package com.example.estechapp.ui.alumnoUI.grupo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.estechapp.databinding.FragmentGrupoAlumnoBinding

class GrupoFragment : Fragment() {

    private var _binding: FragmentGrupoAlumnoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGrupoAlumnoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerListaAlumnos.adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}