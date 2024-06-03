package com.example.estechapp.ui.profesorUI.grupos.grupoCheck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.estechapp.databinding.FragmentGrupoCheckBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.GrupoAlumnosAdapter

class GrupoCheckFragment : Fragment() {

    private var _binding: FragmentGrupoCheckBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MyViewModel>{
        MyViewModel.MyViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGrupoCheckBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerAlumno.adapter = GrupoAlumnosAdapter(
            listOf(
                Alumno("Ramon")
            )
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}