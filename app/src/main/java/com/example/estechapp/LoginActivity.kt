package com.example.estechapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.estechapp.databinding.LoginMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SharedPreferences debe de guardar el usuario y la contraseña y cuando se inicie la aplicación instantaneamente iniciar sesión

        binding.login.setOnClickListener {
            val user = binding.user.text.toString()
            val pass = binding.pass.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(applicationContext, "Campos vacios", Toast.LENGTH_SHORT).show();
            } else {
                val intent = Intent(this, AlumnoActivity::class.java)
                startActivity(intent)
            }
        }
    }
}