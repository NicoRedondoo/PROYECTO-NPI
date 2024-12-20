package com.example.proyectonpi

import android.os.Bundle
import android.widget.Button
import android.content.Intent


class MiPerfilActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miperfil) // Vincula el archivo XML

        setupListeners()
    }

    private fun setupListeners() {
        val horarioPersonalizadoButton = findViewById<Button>(R.id.btnHorarioPersonalizado)
        horarioPersonalizadoButton.setOnClickListener {
            val intent = Intent(this, HorarioPersonalizadoActivity::class.java)
            startActivity(intent)
        }

        val consultaDeExpedienteButton = findViewById<Button>(R.id.btnConsultaExpediente)
        consultaDeExpedienteButton.setOnClickListener {
            val intent = Intent(this, ConsultaDeExpedienteActivity::class.java)
            startActivity(intent)
        }

        val consultaMatriculaButton = findViewById<Button>(R.id.btnConsultaMatricula)
        consultaMatriculaButton.setOnClickListener {
            val intent = Intent(this, ConsultaMatriculaActivity::class.java)
            startActivity(intent)
        }

        val misProfesoresButton = findViewById<Button>(R.id.btnMisProfesores)
        misProfesoresButton.setOnClickListener {
            val intent = Intent(this, MisProfesoresActivity::class.java)
            startActivity(intent)
        }

        val consultaEstadoAccesoPradoButton = findViewById<Button>(R.id.btnConsultaPrado)
        consultaEstadoAccesoPradoButton.setOnClickListener {
            val intent = Intent(this, ConsultaEstadoAccesoPradoActivity::class.java)
            startActivity(intent)
        }

        val accesoPradoButton = findViewById<Button>(R.id.btnAccesoPrado)
        accesoPradoButton.setOnClickListener {
            val intent = Intent(this, AccesoPradoActivity::class.java)
            startActivity(intent)
        }

        val accesoSwadButton = findViewById<Button>(R.id.btnAccesoSWAD)
        accesoSwadButton.setOnClickListener {
            val intent = Intent(this, AccesoSwadActivity::class.java)
            startActivity(intent)
        }

        val consultaSaldoButton = findViewById<Button>(R.id.btnConsultaSaldo)
        consultaSaldoButton.setOnClickListener {
            val intent = Intent(this, ConsultarSaldoActivity::class.java)
            startActivity(intent)
        }
    }
}
