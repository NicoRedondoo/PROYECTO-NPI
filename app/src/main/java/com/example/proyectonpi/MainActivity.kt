package com.example.proyectonpi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.Button
import com.example.proyectonpi.ui.vistas.Pantalla1
import java.util.Locale




// Asegúrate de que el paquete coincida con el de tu proyecto


class MainActivity : AppCompatActivity() {
    private fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
        // Aquí podrías recargar la actividad o cambiar el contenido de la UI si es necesario
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
            // Mostrar un mensaje Toast cuando se toque la pantalla
            val message = getString(R.string.screen_touch_message2)  // Usar la cadena traducida
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, Pantalla1::class.java)
            startActivity(intent)
        }
    }
}

