package com.example.estechapp.ui.profesorUI.fichaje.consultarFichaje

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.data.models.DataCheckInResponse
import com.example.estechapp.data.models.FichajePair
import com.example.estechapp.databinding.FragmentConsultaFichajeBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.FichajesAdapter

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

        //Recibo los datos.
        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        //Voy preparando el recyclerview.
        val recyclerView = binding.recyclerFichajes
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        viewModel.getCheckIn("Bearer $token", id)

        //Aqui me hice un fichajePairs que contiene el fichaje entrada(true) y salida(false).
        //Para poder poner en la misma fila del recyclerview a que hora fue la entrada y salida.
        viewModel.liveDataCheckInList.observe(viewLifecycleOwner, Observer {
            val fichajePairs = ArrayList<FichajePair>()
            var fichajeTrue: DataCheckInResponse? = null
            var fichajeFalse: DataCheckInResponse? = null

            //Esto es para ordenarmelos por id mayor a menor.
            val sortedList = it.sortedByDescending { it -> it.id }


            for (fichaje in sortedList) {
                if (fichajePairs.size >= 12) break

                //Si es true se guarda en fichajetrue y si es false se guarda en fichajefalse.
                if (fichaje.checkIn) {
                    fichajeTrue = fichaje
                } else {
                    fichajeFalse = fichaje
                }

                //Cuando tenga fichajetrue y fichajefalse los mete en fichajepairs
                if (fichajeTrue != null && fichajeFalse != null) {
                    fichajePairs.add(FichajePair(fichajeTrue, fichajeFalse))
                    fichajeTrue = null
                    fichajeFalse = null
                } else if (fichajeTrue != null) {
                    //Aqui cuando solo hay entrada mete los datos en fichajepairs.
                    fichajePairs.add(FichajePair(fichajeTrue, null))
                    fichajeTrue = null
                }
            }

            val adapter = FichajesAdapter(fichajePairs)
            recyclerView.adapter = adapter
        })
    }
}