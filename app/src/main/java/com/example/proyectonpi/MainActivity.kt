package com.example.proyectonpi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.Button
import java.util.Locale
import android.content.Intent
import android.content.res.Configuration



class MainActivity : AppCompatActivity() {
    private fun setLanguage(languageCode: String) {
        // Guardar el idioma seleccionado en SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language", languageCode)
        editor.apply()

        val locale = Locale(languageCode)   // Crear un objeto Locale con el código de idioma indicado
        Locale.setDefault(locale)   // Establecer el idioma predeterminado global de la aplicación
        val config = resources.configuration    // Obtener la configuración actual de los recursos
        config.setLocale(locale)    // Establecer el idioma de la configuración de los recursos
        val context = createConfigurationContext(config)    // Crear un nuevo contexto con la configuración modificada
        resources.updateConfiguration(config, resources.displayMetrics) // Establecer el contexto actualizado en la actividad
        recreate()  // Reacargar la actividad para aplicar el cambio de idioma
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencia al ConstraintLayout
        val mainLayout = findViewById<ConstraintLayout>(R.id.main_layout)

        val btnSpanish = findViewById<Button>(R.id.btn_spanish)
        val btnEnglish = findViewById<Button>(R.id.btn_english)
        val btnFrench = findViewById<Button>(R.id.btn_french)
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

