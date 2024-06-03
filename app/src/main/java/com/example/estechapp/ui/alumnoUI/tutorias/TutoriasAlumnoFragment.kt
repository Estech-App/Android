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
import com.example.estechapp.databinding.FragmentTutoriasAlumnoBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.TutoriasAsignadasAdapter

class TutoriasAlumnoFragment : Fragment() {

    private var _binding: FragmentTutoriasAlumnoBinding? = null

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

        viewModel.getMentoringStudent("Bearer $token", id)

        //Esto es para recibir todas las tutorias por id.
        viewModel.liveDataMentoring.observe(viewLifecycleOwner, Observer { it ->
            if (it != null) {

                //Esto es para que cuando seas alumno ver el nombre del profesor en la tutoria.
                val editor = pref.edit()
                editor.putBoolean("student", true)
                editor.commit()

                //Con el roomId consigo el roomName.
                for (mentoring in it) {
                    viewModel.getRoomId("Bearer $token", mentoring.roomId)
                    mentoring.roomName = pref.getString("room", "")!!
                    mentoring.studentAndroid = pref.getBoolean("student", true)
                }

                //Que solo muestre las approved o modified.
                //Error. El alumno no puede ver las pending.
                val filteredMentorings = it.filter {

                    val isStatusValid = it.status == "APPROVED" || it.status == "MODIFIED" || it.status == "PENDING"

                    isStatusValid
                }

                val adapter = TutoriasAsignadasAdapter(filteredMentorings)
                recyclerView.adapter = adapter

            }
        })

        //Le pasa el roomId y recibe el nombre del aula.
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