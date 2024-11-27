package com.example.proyectonpi.ui.vistas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonpi.R
import com.example.proyectonpi.ui.vistas.CircularMenuView

class Pantalla1 : AppCompatActivity(), CircularMenuView.OnOptionSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla1_layout)

        val circularMenu = findViewById<CircularMenuView>(R.id.circularMenu)
        circularMenu.setOnOptionSelectedListener(this)
    }

    override fun onOptionSelected(option: String) {
        Toast.makeText(this, "Opción seleccionada: $option", Toast.LENGTH_SHORT).show()
    }
}
