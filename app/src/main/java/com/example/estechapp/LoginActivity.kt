package com.example.estechapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.estechapp.databinding.LoginMainBinding
import com.example.estechapp.ui.MyViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginMainBinding

    private val viewModel by viewModels<MyViewModel> {
        MyViewModel.MyViewModelFactory(this)
    }

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SharedPreferences debe de guardar el usuario y la contraseña y cuando se inicie la aplicación instantaneamente iniciar sesión

        val pref = getSharedPreferences("user", Context.MODE_PRIVATE)
        val mailInicio = pref.getString("mail", "")

        binding.user.setText(mailInicio)

        binding.login.setOnClickListener {
            val user = binding.user.text.toString()
            val pass = binding.pass.text.toString()

            if (user.isEmpty() && pass.isEmpty()) {

                binding.user.error = "Introduce el correo"

                binding.pass.error = "Introduce la contraseña"

            } else if (user.isEmpty()) {

                binding.user.error = "Introduce el correo"

            } else if (pass.isEmpty()) {

                binding.pass.error = "Introduce la contraseña"

                //Toast.makeText(applicationContext, "El campo de la contraseña esta vacio", Toast.LENGTH_SHORT).show()

            } else {

                val editor = pref.edit()
                editor.putString("user", user)
                editor.commit()

                viewModel.postLogin(user, pass)

            }
        }

        viewModel.liveDataLogin.observe(this, Observer { response ->
            val user = pref.getString("user", "")
            if (response.roles[0].authority == "ROLE_TEACHER") {
                val editor = pref.edit()
                editor.putString("mail", user)
                editor.putString("token", response.token)
                editor.commit()
                val mail = pref.getString("mail", "")
                val token = pref.getString("token", "")
                if (token != null && mail != null) {
                    viewModel.postEmail("Bearer $token", mail)

                    viewModel.liveDataUserInfo.observe(this, Observer { response2 ->
                        editor.putString("username", response2.name)
                        editor.putString("lastname", response2.lastname)
                        editor.putInt("id", response2.id)
                        editor.commit()
                    })

                    viewModel.liveDataUserInfoError.observe(this, Observer {
                    })
                }
                val intent = Intent(this, ProfesorActivity::class.java)
                startActivity(intent)
                finish()
            } else if (response.roles[0].authority == "ROLE_STUDENT") {
                val editor = pref.edit()
                editor.putString("mail", user)
                editor.putString("token", response.token)
                editor.commit()
                val mail = pref.getString("mail", "")
                val token = pref.getString("token", "")
                if (token != null && mail != null) {
                    viewModel.postEmail("Bearer $token", mail)

                    viewModel.liveDataUserInfo.observe(this, Observer { response2 ->
                        editor.putString("username", response2.name)
                        editor.putString("lastname", response2.lastname)
                        editor.putInt("id", response2.id)
                        editor.commit()
                    })

                    viewModel.liveDataUserInfoError.observe(this, {

                    })
                }
                val intent = Intent(this, AlumnoActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.alert_response, null)
                val textview = view.findViewById<TextView>(R.id.textView16)
                textview.setBackgroundResource(R.drawable.rounded_textview_error)
                textview.setText("Este usuario no puede iniciar sesion en Movil")
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                Handler(Looper.getMainLooper()).postDelayed({
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }, 5000)
            }
        })

        viewModel.liveDataLoginError.observe(this) {

            binding.user.error = "El correo o la contraseña son incorrectos"

            binding.pass.error = "El correo o la contraseña son incorrectos"

        }

    }
}

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