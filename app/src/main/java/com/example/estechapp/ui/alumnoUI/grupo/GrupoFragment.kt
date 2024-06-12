package com.example.estechapp.ui.alumnoUI.grupo

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estechapp.data.models.UserConRol
import com.example.estechapp.data.models.UserNombres
import com.example.estechapp.databinding.FragmentGrupoAlumnoBinding
import com.example.estechapp.ui.MyViewModel
import com.example.estechapp.ui.adapter.GrupoAlumnosAdapter
import com.example.estechapp.ui.adapter.GrupoAlumnosAdapter2

class GrupoFragment : Fragment() {

    private var _binding: FragmentGrupoAlumnoBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: GrupoAlumnosAdapter

    private lateinit var adapter2: GrupoAlumnosAdapter2

    private var alumnos: List<UserConRol> = listOf()

    private var profesores: List<UserConRol> = listOf()

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrupoAlumnoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Obtiene la altura de la pantalla
        val screenHeight = requireActivity().resources.displayMetrics.heightPixels

        // Añade un OnGlobalLayoutListener a la vista raíz
        root.viewTreeObserver.addOnGlobalLayoutListener {
            // Obtiene el tamaño de la pantalla visible
            val rect = Rect()
            root.getWindowVisibleDisplayFrame(rect)
            val visibleScreenHeight = rect.bottom - rect.top

            // Calcula la altura del teclado
            val keyboardHeight = screenHeight - visibleScreenHeight

            // Si el teclado está abierto y BuscarProfesor está en foco, desplaza la vista hacia arriba
            if (keyboardHeight > screenHeight * 0.15 && binding.BuscarProfesor.hasFocus()) {
                root.scrollTo(0, keyboardHeight)
            } else {
                // Si el teclado está cerrado, desplaza la vista hacia abajo
                root.scrollTo(0, 0)
            }
        }

        // Inicializa los adaptadores aquí
        adapter = GrupoAlumnosAdapter(alumnos)
        adapter2 = GrupoAlumnosAdapter2(profesores)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        val id = pref.getInt("id", 0)

        val recyclerView = binding.recyclerListaAlumnos
        val llm = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = llm

        val recyclerView2 = binding.recyclerListaProfesores
        val llm2 = LinearLayoutManager(requireContext())
        recyclerView2.layoutManager = llm2

        viewModel.getUserStudent("Bearer $token", id)

        viewModel.liveDataUserGroups.observe(viewLifecycleOwner, Observer {
            val alumnosMutable = mutableListOf<UserConRol>()
            val profesoresMutable = mutableListOf<UserConRol>()

            var posicion1 = 1
            var posicion2 = 1

            for (grupos in it.groups) {
                for (users in grupos.users) {
                    if (users.role == "STUDENT") {
                        users.posicion = posicion1
                        posicion1++
                        alumnosMutable.add(users)
                    } else if (users.role == "TEACHER") {
                        users.posicion = posicion2
                        profesoresMutable.add(users)
                        posicion2++
                    }
                }
            }

            alumnos = alumnosMutable.toList()
            profesores = profesoresMutable.toList()


            adapter = GrupoAlumnosAdapter(alumnos)
            recyclerView.adapter = adapter
            adapter2 = GrupoAlumnosAdapter2(profesores)
            recyclerView2.adapter = adapter2

            adapter.enviarCorreoListener = object : GrupoAlumnosAdapter.EnviarCorreoListener {
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

            adapter2.enviarCorreoListener = object : GrupoAlumnosAdapter2.EnviarCorreoListener {
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
        binding.BuscarAlumno.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                filtrarAlumnos(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No necesitas hacer nada aquí
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No necesitas hacer nada aquí
            }
        })

        // Agrega un TextWatcher a tu EditText para filtrar la lista de profesores cuando el texto cambie
        binding.BuscarProfesor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                filtrarProfesores(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No necesitas hacer nada aquí
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No necesitas hacer nada aquí
            }
        })
    }

    fun filtrarAlumnos(texto: String) {
        val alumnosFiltrados = alumnos.filterIndexed { index, it ->
            it.name.contains(texto, ignoreCase = true) || (it.posicion).toString().contains(texto)
        }
        adapter.updateData(alumnosFiltrados)
    }

    fun filtrarProfesores(texto: String) {
        val profesoresFiltrados = profesores.filterIndexed { index, it ->
            it.name.contains(texto, ignoreCase = true) || (it.posicion).toString().contains(texto)
        }
        adapter2.updateData(profesoresFiltrados)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
