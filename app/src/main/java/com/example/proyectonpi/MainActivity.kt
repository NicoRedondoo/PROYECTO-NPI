package com.example.proyectonpi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.Button
import java.util.Locale
import android.content.Intent
import android.content.res.Configuration


// Asegúrate de que el paquete coincida con el de tu proyecto


class MainActivity : AppCompatActivity() {
    private fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        // Actualizar la configuración del contexto actual
        baseContext.createConfigurationContext(config)

        // Reacargar actividad
        recreate()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencia al ConstraintLayout
        val mainLayout = findViewById<ConstraintLayout>(R.id.main_layout)

        val btnSpanish = findViewById<Button>(R.id.btn_spanish)
        val btnEnglish = findViewById<Button>(R.id.btn_english)
        val btnFrench = findViewById<Button>(R.id.btn_french)
        val btnGerman = findViewById<Button>(R.id.btn_german)
        val btnItalian = findViewById<Button>(R.id.btn_italian)
        val btnPortuguese = findViewById<Button>(R.id.btn_portuguese)

        btnSpanish.setOnClickListener {
            setLanguage("es")
        }
        btnEnglish.setOnClickListener {
            setLanguage("en")
        }
        btnFrench.setOnClickListener {
            setLanguage("fr")
        }
        btnGerman.setOnClickListener {
            setLanguage("de")
        }
        btnItalian.setOnClickListener {
            setLanguage("it")
        }
        btnPortuguese.setOnClickListener {
            setLanguage("pt")
        }

        // Configurar el listener de clic para detectar cuando se toca la pantalla
        mainLayout.setOnClickListener {
//            // Crear un Intent para iniciar la nueva actividad
            val intent = Intent(this@MainActivity, Pantalla1::class.java)
//
//            // Iniciar la nueva actividad
            startActivity(intent)

            // Mostrar un mensaje Toast
            val message = getString(R.string.screen_touch_message2)
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }


    }
}

