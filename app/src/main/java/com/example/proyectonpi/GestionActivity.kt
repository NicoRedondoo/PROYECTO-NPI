package com.example.proyectonpi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectonpi.ui.theme.PROYECTONPITheme

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

        val emisionTUIButton = findViewById<Button>(R.id.btnEmisionTarjetaTUI)
        emisionTUIButton.setOnClickListener {
            val intent = Intent(this, EmisionTUIActivity::class.java)
            startActivity(intent)
        }

        val gestionFotoButton = findViewById<Button>(R.id.btnGestionFotoUGR)
        gestionFotoButton.setOnClickListener {
            val intent = Intent(this, GestionDeMiFotoUGRActivity::class.java)
            startActivity(intent)
        }

        val resguardoMatriculaButton = findViewById<Button>(R.id.btnResguardoMatricula)
        resguardoMatriculaButton.setOnClickListener {
            val intent = Intent(this, ResguardoMatriculaActivity::class.java)
            startActivity(intent)
        }

        val solicitudBecasButton = findViewById<Button>(R.id.btnSolicitudBecas)
        solicitudBecasButton.setOnClickListener {
            val intent = Intent(this, SolicitudBecasActivity::class.java)
            startActivity(intent)
        }

        val tramitacionTitulosButton = findViewById<Button>(R.id.btnTramitacionTitulos)
        tramitacionTitulosButton.setOnClickListener {
            val intent = Intent(this, TramitacionDeTitulosActivity::class.java)
            startActivity(intent)
        }

        val misPagosButton = findViewById<Button>(R.id.btnMisPagosUGR)
        misPagosButton.setOnClickListener {
            val intent = Intent(this, MisPagosActivity::class.java)
            startActivity(intent)
        }

    }
}

