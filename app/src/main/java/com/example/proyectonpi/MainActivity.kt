package com.example.proyectonpi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


// Asegúrate de que el paquete coincida con el de tu proyecto


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencia al ConstraintLayout
        val mainLayout = findViewById<ConstraintLayout>(R.id.main_layout)

        // Configurar el listener de clic para detectar cuando se toca la pantalla
        mainLayout.setOnClickListener {
            // Mostrar un mensaje Toast cuando se toque la pantalla
            Toast.makeText(this@MainActivity, "Pantalla tocada. Comenzando...", Toast.LENGTH_SHORT)
                .show()

            // Opcional: puedes agregar el Intent para pasar a la siguiente actividad
            // Intent intent = new Intent(MainActivity.this, NextActivity.class);
            // startActivity(intent);
        }
    }
}

