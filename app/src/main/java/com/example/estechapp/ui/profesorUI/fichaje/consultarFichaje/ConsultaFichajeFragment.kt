package com.example.estechapp.ui.profesorUI.fichaje.consultarFichaje

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.R
import com.example.estechapp.data.models.DataCheckInResponse
import com.example.estechapp.data.models.FichajePair
import com.example.estechapp.databinding.FragmentConsultaFichajeBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.profesorUI.Fichaje
import com.example.estechapp.databinding.FragmentConsultaFichajeBinding
import com.example.estechapp.ui.adapter.FichajesAdapter
import com.example.estechapp.data.models.Fichaje

class ConsultaFichajeFragment : Fragment() {

    private var _binding: FragmentConsultaFichajeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConsultaFichajeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.recyclerFichajes.adapter = FichajesAdapter(
            listOf(
                Fichaje(1,"Enero", 2023, "19:00")
            )
        )*/

        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        val recyclerView = binding.recyclerFichajes
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        viewModel.getCheckIn("Bearer $token", id)

        viewModel.liveDataCheckInList.observe(viewLifecycleOwner, Observer {
            val fichajePairs = ArrayList<FichajePair>()
            var fichajeTrue: DataCheckInResponse? = null
            var fichajeFalse: DataCheckInResponse? = null

            for (fichaje in it) {
                if (fichajePairs.size >= 12) break // Si ya tienes 10 pares, detén el bucle

                if (fichaje.checkIn) {
                    fichajeTrue = fichaje
                } else {
                    fichajeFalse = fichaje
                }

                if (fichajeTrue != null && fichajeFalse != null) {
                    fichajePairs.add(FichajePair(fichajeTrue, fichajeFalse))
                    fichajeTrue = null
                    fichajeFalse = null
                } else if (fichajeTrue != null) {
                    fichajePairs.add(FichajePair(fichajeTrue, null))
                    fichajeTrue = null
                }
            }

            val adapter = FichajesAdapter(fichajePairs)
            recyclerView.adapter = adapter
        })
    }
}