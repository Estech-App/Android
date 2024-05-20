package com.example.estechapp.ui.alumnoUI.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.R
import com.example.estechapp.databinding.FragmentHomeAlumnoBinding
import com.example.estechapp.ui.MyViewModel

class HomeAlumnoFragment : Fragment() {

    private var _binding: FragmentHomeAlumnoBinding? = null

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(requireContext())
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeAlumnoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = pref.getString("username", "")
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        val recyclerView = binding.recyclerTutoriasHot
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        viewModel.getMentoringTeacher("Bearer $token", id)
    }
}