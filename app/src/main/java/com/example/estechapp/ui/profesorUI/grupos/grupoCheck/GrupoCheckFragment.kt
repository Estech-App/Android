package com.example.estechapp.ui.profesorUI.grupos.grupoCheck

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.data.models.UserConRol
import com.example.estechapp.databinding.FragmentGrupoCheckBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.GrupoAlumnosAdapter
import com.example.estechapp.ui.adapter.GrupoProfesorAdapter

class GrupoCheckFragment : Fragment() {

    private var _binding: FragmentGrupoCheckBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GrupoProfesorAdapter

    private var alumnos: List<UserConRol> = listOf()

    private var todosUsuarios: List<UserConRol> = listOf()

    private val viewModel by viewModels<MyViewModel> {
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

        //Recibo los datos.
        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        val recyclerView = binding.recyclerAlumno
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        // Recupera el ID del grupo del Bundle
        val grupoId = arguments?.getInt("grupoId")

        viewModel.getGroupUser("Bearer $token", id)

        viewModel.liveDataGroupUser.observe(viewLifecycleOwner, Observer {

            val profesoresMutable = mutableListOf<UserConRol>()
            val alumnosMutable = mutableListOf<UserConRol>()

            var contador = 1

            for (grupos in it) {
                if (grupos.id == grupoId) {
                    for (users in grupos.users) {
                        if (users.role == "TEACHER") {
                            users.posicion = contador
                            contador++
                            profesoresMutable.add(users)
                        } else {
                            alumnosMutable.add(users)
                        }
                    }
                }
            }

            // Ahora asignamos números a los alumnos
            for (alumno in alumnosMutable) {
                alumno.posicion = contador
                contador++
            }

            // Combinamos las listas
            todosUsuarios = profesoresMutable + alumnosMutable

            adapter = GrupoProfesorAdapter(todosUsuarios)
            recyclerView.adapter = adapter

            adapter.enviarCorreoListener = object : GrupoProfesorAdapter.EnviarCorreoListener {
                override fun enviarCorreo(item: UserConRol) {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data =
                            Uri.parse("mailto:") // solo las aplicaciones de correo deberían manejar esto
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(item.email)) // destinatarios
                        putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo")
                        putExtra(Intent.EXTRA_TEXT, "Hola, ${item.name} ${item.lastname}")
                    }
                    requireContext().startActivity(
                        Intent.createChooser(
                            intent,
                            "Elige una aplicación de correo"
                        )
                    )
                }
            }

        })

        // Agrega un TextWatcher a tu EditText para filtrar la lista de alumnos cuando el texto cambie
        binding.buscadorAlumno.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                filtrarUsuarios(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No necesitas hacer nada aquí
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No necesitas hacer nada aquí
            }
        })
    }

    fun filtrarUsuarios(texto: String) {
        val usuariosFiltrados = todosUsuarios.filterIndexed { index, it ->
            it.name.contains(texto, ignoreCase = true) || (it.posicion).toString().contains(texto)
        }
        adapter.updateData(usuariosFiltrados)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}