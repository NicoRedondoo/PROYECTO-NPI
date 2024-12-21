package com.example.proyectonpi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class CitaSecretariaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cita_secretaria) // Vincula el archivo XML
        setupListeners()
    }
    private fun setupListeners() {
        val confirmarCitaButton = findViewById<Button>(R.id.btnConfirmarCita)
        confirmarCitaButton.setOnClickListener {
            Toast.makeText(this, "Cita Confirmada", Toast.LENGTH_LONG).show()
        }
    }
}