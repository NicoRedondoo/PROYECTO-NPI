package com.example.proyectonpi

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class GestionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion) // Vincula el archivo XML
        setupListeners()
    }

    private fun setupListeners() {
        val citaEnSecretariaButton = findViewById<Button>(R.id.btnCitaSecretaria)
        citaEnSecretariaButton.setOnClickListener {
            val intent = Intent(this, CitaSecretariaActivity::class.java)
            startActivity(intent)
        }

        val solicitudGeneralButton = findViewById<Button>(R.id.btnSolicitudGeneral)
        solicitudGeneralButton.setOnClickListener {
            val intent = Intent(this, SolicitudGeneralActivity::class.java)
            startActivity(intent)
        }

        val certificadoCalificacionesButton = findViewById<Button>(R.id.btnCertificadoCalificaciones)
        certificadoCalificacionesButton.setOnClickListener {
            val intent = Intent(this, CertificadoCalificacionesActivity::class.java)
            startActivity(intent)
        }

        val certificadoDeMatriculasButton = findViewById<Button>(R.id.btnCertificadoDeMatricula)
        certificadoDeMatriculasButton.setOnClickListener {
            val intent = Intent(this, CertificadoDeMatriculaActivity::class.java)
            startActivity(intent)
        }



    }
}

