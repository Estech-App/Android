package com.example.estechapp.ui.profesorUI.fichaje.consultarFichaje

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.estechapp.databinding.FragmentConsultaFichajeBinding
import com.example.estechapp.ui.adapter.FichajesAdapter
import com.example.estechapp.data.models.Fichaje

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConsultaFichajeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultaFichajeFragment : Fragment() {

    private var _binding: FragmentConsultaFichajeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.recyclerFichajes.adapter = FichajesAdapter(
            listOf(
                Fichaje(1,"Enero", 2023, "19:00")
            )
        )

    }
}