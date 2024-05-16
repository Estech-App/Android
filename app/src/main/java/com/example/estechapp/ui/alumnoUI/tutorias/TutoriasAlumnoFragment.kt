package com.example.estechapp.ui.alumnoUI.tutorias

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.data.models.Tutoria
import com.example.estechapp.databinding.FragmentTutoriasAlumnoBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapter
import com.example.estechapp.ui.adapter.TutoriasHotAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class TutoriasAlumnoFragment : Fragment() {

    private var _binding: FragmentTutoriasAlumnoBinding? = null

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

        _binding = FragmentTutoriasAlumnoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        val recyclerView = binding.recyclerTutoriasAlumno
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        viewModel.getMentoringStudent("Bearer $token", id)

        viewModel.liveDataMentoring.observe(viewLifecycleOwner, Observer { it ->
            if (it != null) {

                val filteredMentorings = it.filter {

                    val isStatusValid = it.status == "APPROVED" || it.status == "MODIFIED" || it.status == "PENDING"

                    isStatusValid
                }

                val adapter = TutoriasAsignadasAdapter(filteredMentorings)
                recyclerView.adapter = adapter

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}