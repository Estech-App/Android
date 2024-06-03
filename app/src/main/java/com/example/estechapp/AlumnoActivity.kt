package com.example.estechapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.estechapp.databinding.AlumnoMainBinding

class AlumnoActivity : AppCompatActivity() {

    /* Aqui solo esta el navigation view del alumno */

    private lateinit var binding: AlumnoMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AlumnoMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_alumno_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tutorias_alumno,
                R.id.navigation_practicaLibre,
                R.id.navigation_grupo_alumno,
                R.id.navigation_home_alumno
            )
        )
        navView.setupWithNavController(navController)
    }
}