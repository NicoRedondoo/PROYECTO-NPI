package com.example.proyectonpi

import android.content.Intent
import android.os.Bundle
import android.widget.Button

class InformacionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion) // Vincula el archivo XML

        setupListeners()
    }

    private fun setupListeners() {
        val calendarioDocenteButton = findViewById<Button>(R.id.btnCalendarioDocente)
        calendarioDocenteButton.setOnClickListener {
            val intent = Intent(this, CalendarioDocenteActivity::class.java)
            startActivity(intent)
        }

        val calendarioExamenesButton = findViewById<Button>(R.id.btnCalendarioExamenes)
        calendarioExamenesButton.setOnClickListener {
            val intent = Intent(this, CalendarioExamenesActivity::class.java)
            startActivity(intent)
        }

        val calendarioTramitesButton = findViewById<Button>(R.id.btnCalendarioTramites)
        calendarioTramitesButton.setOnClickListener {
                val intent = Intent(this, CalendarioTramitesActivity::class.java)
            startActivity(intent)
        }

        val horarioProfesoresTutoriasButton = findViewById<Button>(R.id.btnHorarios)
        horarioProfesoresTutoriasButton.setOnClickListener {
            val intent = Intent(this, HorarioProfesoresTutoriasActivity::class.java)
            startActivity(intent)
        }

        val bibliotecaButton = findViewById<Button>(R.id.btnBiblioteca)
        bibliotecaButton.setOnClickListener {
            val intent = Intent(this, BibliotecaActivity::class.java)
            startActivity(intent)
        }

    }
}
