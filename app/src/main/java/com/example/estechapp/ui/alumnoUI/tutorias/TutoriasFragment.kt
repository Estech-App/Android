<<<<<<<< HEAD:app/src/main/java/com/example/estechapp/ui/alumnoUI/tutorias/TutoriasFragment.kt
package com.example.estechapp.ui.alumnoUI.tutorias
========
package com.example.estechapp.ui.profesorUI.tutorias
>>>>>>>> origin/ramon:app/src/main/java/com/example/estechapp/ui/profesorUI/tutorias/TutoriasFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.estechapp.databinding.FragmentTutoriasAlumnoBinding

class TutoriasFragment : Fragment() {

    private var _binding: FragmentTutoriasAlumnoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

<<<<<<<< HEAD:app/src/main/java/com/example/estechapp/ui/alumnoUI/tutorias/TutoriasFragment.kt
        _binding = FragmentTutoriasAlumnoBinding.inflate(inflater, container, false)
        val root: View = binding.root
========
        _binding = FragmentTutoriasBinding.inflate(inflater, container, false)
>>>>>>>> origin/ramon:app/src/main/java/com/example/estechapp/ui/profesorUI/tutorias/TutoriasFragment.kt

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}