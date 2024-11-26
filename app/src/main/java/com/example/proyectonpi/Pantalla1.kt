package com.example.proyectonpi

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Pantalla1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla1_layout)

        // Configurar listeners para los botones
        findViewById<Button>(R.id.option_1).setOnClickListener {
            Toast.makeText(this, "Opción 1 seleccionada", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.option_2).setOnClickListener {
            Toast.makeText(this, "Opción 2 seleccionada", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.option_3).setOnClickListener {
            Toast.makeText(this, "Opción 3 seleccionada", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.option_4).setOnClickListener {
            Toast.makeText(this, "Opción 4 seleccionada", Toast.LENGTH_SHORT).show()
        }
    }
}
