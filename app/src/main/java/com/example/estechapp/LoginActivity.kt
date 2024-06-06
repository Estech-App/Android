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

        /* Voy a usar mucho el getSharedPreferences para pasarme datos entre las vistas
        Aqui voy a hacer que el correo se guarde y cada vez que abres la app no tienes que ponerlo
        solo la contraseña */
        val pref = getSharedPreferences("user", Context.MODE_PRIVATE)
        val mailInicio = pref.getString("mail", "")

        //Aqui si ya has iniciado sesion anteriormente se te pone el correo
        binding.user.setText(mailInicio)

        //Aqui si los campos estan vacios salen los .error con mensajes.
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

            } else {

                //Aqui guarda el correo
                val editor = pref.edit()
                editor.putString("user", user)
                editor.commit()

                viewModel.postLogin(user, pass)

            }
        }

        //Este es el livedata del login.
        viewModel.liveDataLogin.observe(this, Observer { response ->
            //Recibe el user
            val user = pref.getString("user", "")
            if (response.roles[0].authority == "ROLE_TEACHER") {
                //Este if es cuando el login lo hace el profesor
                //Se guarda los datos y hace el postEmail
                val editor = pref.edit()
                editor.putBoolean("profesor", true)
                editor.putString("mail", user)
                editor.putString("token", response.token)
                editor.commit()
                val mail = pref.getString("mail", "")
                val token = pref.getString("token", "")
                if (token != null && mail != null) {
                    viewModel.postEmail("Bearer $token", mail)
                }
            } else if (response.roles[0].authority == "ROLE_STUDENT") {
                //Este else if es cuando el login lo hace el alumno
                //Se guarda los datos y hace el postEmail
                val editor = pref.edit()
                editor.putBoolean("profesor", false)
                editor.putString("mail", user)
                editor.putString("token", response.token)
                editor.commit()
                val mail = pref.getString("mail", "")
                val token = pref.getString("token", "")
                if (token != null && mail != null) {
                    viewModel.postEmail("Bearer $token", mail)
                }
            } else {
                //Este else es cuando el login es correcto pero es secretaria o admin
                //Te sale un alertdialog en rojo con el mensaje
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

        //Esto es cuando de error el login. Porque el correo o la contraseña son incorrectos.
        viewModel.liveDataLoginError.observe(this) {

            binding.user.error = "El correo o la contraseña son incorrectos"

            binding.pass.error = "El correo o la contraseña son incorrectos"

        }

        val editor = pref.edit()

        /*Este es el livedata del userinfo.
        Me guardo los datos del usuario y si es profesor hace intent a profesorActivity
        y si es alumno pues lo mismo pero del alumno */
        viewModel.liveDataUserInfo.observe(this, Observer { response2 ->
            editor.putString("username", response2.name)
            editor.putString("lastname", response2.lastname)
            editor.putInt("id", response2.id)
            editor.commit()
            val profesor = pref.getBoolean("profesor", true)
            if (profesor) {
                val intent = Intent(this, ProfesorActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, AlumnoActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        //Esto es cuando el UserInfo da error. Que no deberia de pasar.
        viewModel.liveDataUserInfoError.observe(this, Observer {
            Toast.makeText(applicationContext, "No se ha podido obtener la informacion del usuario", Toast.LENGTH_SHORT).show()
        })

    }
}