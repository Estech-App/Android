package com.example.estechapp.ui.profesorUI.tutorias

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.estechapp.R
import com.example.estechapp.databinding.FragmentTutoriasBinding

class TutoriasFragment : Fragment() {

    private var _binding: FragmentTutoriasBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTutoriasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.alert, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val cancelar = view.findViewById<Button>(R.id.button)
        val enviar = view.findViewById<Button>(R.id.button4)
        val titulo = view.findViewById<TextView>(R.id.textView7)
        val mensaje = view.findViewById<TextView>(R.id.textView15)
        titulo.setText("Eliminar tutoria")
        mensaje.setText("¿Seguro que desea eliminar esta tutoria?")
        enviar.setText("Confirmar")
        cancelar.setOnClickListener {
            dialog.dismiss()
        }
        enviar.setOnClickListener {

            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            val mensaje = view2.findViewById<TextView>(R.id.textView16)
            mensaje.setText("Tutoria eliminada con exito!")
            builder2.setView(view2)
            val dialog2 = builder2.create()
            dialog2.show()
            dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Handler(Looper.getMainLooper()).postDelayed({
                if (dialog2.isShowing) {
                    dialog2.dismiss()
                }
            }, 5000)
            dialog.dismiss()
        }*/
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}