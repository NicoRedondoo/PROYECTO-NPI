package com.example.proyectonpi

import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.content.Context

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectonpi.ui.theme.PROYECTONPITheme


class MiPerfilActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miperfil) // Vincula el archivo XML

        setupListeners()
    }

    private fun setupListeners() {
        val horarioPersonalizadoButton = findViewById<Button>(R.id.btnHorarioPersonalizado)
        horarioPersonalizadoButton.setOnClickListener {
            val intent = Intent(this, EscanearActivity::class.java)
            startActivity(intent)
        }

        val consultaSaldoButton = findViewById<Button>(R.id.btnConsultaSaldo)
        consultaSaldoButton.setOnClickListener {
            val intent = Intent(this, ConsultarSaldoActivity::class.java)
            startActivity(intent)
        }
    }
}
