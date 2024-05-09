package com.example.estechapp.ui.profesorUI.grupos

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
import com.example.estechapp.databinding.FragmentGruposBinding
import java.util.Calendar

class GruposFragment : Fragment() {

    private var _binding: FragmentGruposBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        /*binding.recyclerManana.adapter = MananaHorarioAdapter()
        binding.recyclerTarde.adapter = TardeHorarioAdapter()
        binding.recyclerGrupos.adapter = GrupoAdapter()*/

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

                    binding.textView8.text = "$diaSemanaN, $diaMes, $mesN"

                    handler.postDelayed(this, 1000)

                }
            }
        }

        handler.post(runnable)

        /*val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.alert, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val cancelar = view.findViewById<Button>(R.id.button)
        val enviar = view.findViewById<Button>(R.id.button4)
        val titulo = view.findViewById<TextView>(R.id.textView7)
        val mensaje = view.findViewById<TextView>(R.id.textView15)
        titulo.setText("Alert")
        mensaje.setText("¿Desea mandar un mail al alumno Iñigo Acosta Conde?")
        enviar.setText("Enviar")
        cancelar.setOnClickListener {
            dialog.dismiss()
        }
        enviar.setOnClickListener {

            val builder2 = AlertDialog.Builder(requireContext())
            val view2 = layoutInflater.inflate(R.layout.alert_response, null)
            val mensaje = view2.findViewById<TextView>(R.id.textView16)
            mensaje.setText("Mensaje enviado con exito!")
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