package com.example.estechapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.estechapp.databinding.LoginMainBinding
import com.example.estechapp.ui.ViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.estechapp.data.Repository

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginMainBinding

    private val viewModel by viewModels<ViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SharedPreferences debe de guardar el usuario y la contraseña y cuando se inicie la aplicación instantaneamente iniciar sesión

        val pref = getSharedPreferences("user", Context.MODE_PRIVATE)
        val mail = pref.getString("mail","")

        binding.user.setText(mail)

        binding.login.setOnClickListener {
            val user = binding.user.text.toString()
            val pass = binding.pass.text.toString()

            if (user.isEmpty() && pass.isEmpty()) {

                binding.user.error = "Introduce el correo"

                binding.pass.error = "Introduce la contraseña"

                Toast.makeText(applicationContext, "Campos vacios", Toast.LENGTH_SHORT).show();
            } else if (user.isEmpty()) {

                binding.user.error = "Introduce el correo"

                Toast.makeText(applicationContext, "El campo de correo esta vacio", Toast.LENGTH_SHORT).show()

            } else if (pass.isEmpty()) {

                binding.pass.error = "Introduce la contraseña"

                Toast.makeText(applicationContext, "El campo de la contraseña esta vacio", Toast.LENGTH_SHORT).show()

            } else {

                viewModel.postLogin(user, pass)

                viewModel.liveDataLogin.observe(this, Observer { response ->
                    if (response.roles?.get(0)?.authority == "ROLE_TEACHER") {
                        val editor = pref.edit()
                        editor.putString("mail", user)
                        editor.putString("token", response.token)
                        val mail = pref.getString("mail", "")
                        val token = pref.getString("token", "")
                        if (token != null && mail != null) {
                            viewModel.postEmail(token, mail)

                            viewModel.liveDataUserInfo.observe(this, Observer { response ->
                                editor.putString("username", response.name)
                                editor.putString("lastname", response.lastname)
                                editor.putInt("id", response.id)
                            })
                        }
                        Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfesorActivity::class.java)
                    startActivity(intent)
                    } else if (response.roles?.get(0)?.authority == "ROLE_STUDENT") {
                        val editor = pref.edit()
                        editor.putString("mail", user)
                        editor.putString("token", response.token)
                        val mail = pref.getString("mail", "")
                        val token = pref.getString("token", "")
                        if (token != null && mail != null) {
                            viewModel.postEmail(token, mail)

                            viewModel.liveDataUserInfo.observe(this, Observer { response ->
                                editor.putString("username", response.name)
                                editor.putString("lastname", response.lastname)
                                editor.putInt("id", response.id)
                            })
                        }
                        Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AlumnoActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Este usuario no puede iniciar sesion en Movil", Toast.LENGTH_SHORT).show()
                    }
                })

                viewModel.liveDataLoginError.observe(this, Observer { error ->
                    Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}