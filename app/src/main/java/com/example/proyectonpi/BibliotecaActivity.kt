package com.example.proyectonpi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView

class BibliotecaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblioteca) // Vincula el archivo XML
    // Redirigir al enlace
        val url = "https://etsiit.ugr.es/la-escuela/presentacion/instalaciones-servicios/biblioteca"

        // Crear un Intent para abrir el enlace en el navegador
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        // Iniciar el intent cuando el usuario haga clic en el TextView
        val textView: TextView = findViewById(R.id.tvSearchLink)
        textView.setOnClickListener {
            startActivity(intent)
        }
    }
}