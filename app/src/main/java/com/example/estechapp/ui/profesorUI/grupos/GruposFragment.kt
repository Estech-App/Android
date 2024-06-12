package com.example.estechapp.ui.profesorUI.grupos

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.R
import com.example.estechapp.data.models.Grupos
import com.example.estechapp.databinding.FragmentGruposBinding
//import com.example.estechapp.ui.adapter.GrupoAdapter
import com.example.estechapp.ui.adapter.HorarioAdapter
import com.example.estechapp.data.models.Horario
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.GrupoAdapter
import java.util.Calendar

class GruposFragment : Fragment() {

    private var _binding: FragmentGruposBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: GrupoAdapter

    private lateinit var adapter2: HorarioAdapter

    private lateinit var adapter3: HorarioAdapter

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGruposBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Esto es para que se actualice la fecha y hora cada segundo
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {

                _binding?.let { binding ->

                    val calendar = Calendar.getInstance()

                    val diaMes = calendar.get(Calendar.DAY_OF_MONTH)

                    val diaSemana = calendar.get(Calendar.DAY_OF_WEEK)

                    var diaSemanaN = ""

                    val mes = calendar.get(Calendar.MONTH)

                    var mesN = ""

                    //Esto es para saber que numero es cada dia.
                    when (diaSemana) {
                        1 -> {
                            diaSemanaN = "Domingo"
                        }

                        2 -> {
                            diaSemanaN = "Lunes"
                        }

                        3 -> {
                            diaSemanaN = "Martes"
                        }

                        4 -> {
                            diaSemanaN = "Miercoles"
                        }

                        5 -> {
                            diaSemanaN = "Jueves"
                        }

                        6 -> {
                            diaSemanaN = "Viernes"
                        }

                        7 -> {
                            diaSemanaN = "Sabado"
                        }
                    }

                    //Esto es para saber que numero es cada mes.
                    when (mes) {
                        0 -> {
                            mesN = "Enero"
                        }

                        1 -> {
                            mesN = "Febrero"
                        }

                        2 -> {
                            mesN = "Marzo"
                        }

                        3 -> {
                            mesN = "Abril"
                        }

                        4 -> {
                            mesN = "Mayo"
                        }

                        5 -> {
                            mesN = "Junio"
                        }

                        6 -> {
                            mesN = "Julio"
                        }

                        7 -> {
                            mesN = "Agosto"
                        }

                        8 -> {
                            mesN = "Septiembre"
                        }

                        9 -> {
                            mesN = "Octubre"
                        }

                        10 -> {
                            mesN = "Noviembre"
                        }

                        11 -> {
                            mesN = "Diciembre"
                        }
                    }

                    binding.fecha.text = "$diaSemanaN, $diaMes, $mesN"

                    binding.textView8.text = "Horario de hoy"

                    handler.postDelayed(this, 1000)

                }
            }
        }

        handler.post(runnable)

        //Recibo los datos.
        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = pref.getString("username", "")
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        val recyclerView = binding.recyclerGrupos
        val llm = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = llm

        //Voy preparando el recyclerview
        val recyclerView2 = binding.re
        val llm2 = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = llm2

        //Voy preparando el recyclerview
        val recyclerView3 = binding.recyclerViewHorario2
        val llm3 = LinearLayoutManager(requireContext())
        recyclerView3.layoutManager = llm3

        viewModel.getGroupUser("Bearer $token", id)

        viewModel.liveDataGroupUser.observe(viewLifecycleOwner, Observer {

            for (grupos in it) {
                var contador = 0
                for (users in grupos.users) {
                    contador++
                }
                grupos.cantidad = contador
            }

            adapter = GrupoAdapter(it)
            recyclerView.adapter = adapter

            adapter.navegarGrupoListener =
                object : GrupoAdapter.NavegarGrupoListener {
                    override fun navegarGrupo(item: Grupos) {
                        val bundle = Bundle().apply {
                            putInt("grupoId", item.id)
                        }
                        findNavController().navigate(
                            R.id.action_navigation_grupos_to_grupoCheckFragment,
                            bundle
                        )
                    }
                }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}