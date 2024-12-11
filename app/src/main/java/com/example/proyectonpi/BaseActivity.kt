package com.example.proyectonpi
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    // Método para aplicar el idioma guardado
    fun applySavedLanguage() {
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language", "es") // Idioma predeterminado
        val locale = Locale(languageCode!!)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedLanguage() // Aplica el idioma antes de cualquier configuración
        super.onCreate(savedInstanceState)
    }
}
