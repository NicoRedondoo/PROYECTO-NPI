package com.example.proyectonpi
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectonpi.ui.vistas.CircularMenuView

class Pantalla1 : AppCompatActivity() {

    private lateinit var circularMenu: CircularMenuView
    private lateinit var summaryButton: Button
    private lateinit var summaryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla1_layout)

        circularMenu = findViewById(R.id.circularMenu)  // Referencia correcta

        summaryButton = findViewById(R.id.summaryButton)
        summaryText = findViewById(R.id.summaryText)

        circularMenu.setOnOptionSelectedListener(object : CircularMenuView.OnOptionSelectedListener {
            override fun onOptionSelected(option: String) {
                Log.d("Option", option)
                showSummary(option)
            }
        })

        summaryButton.setOnClickListener {
            startActivity(Intent(this, GestionActivity::class.java))
        }
    }

    private fun showSummary(option: String) {
        val summary = when (option) {
            "Gestión" -> "Gestión:\n- Opción 1\n- Opción 2\n- Opción 3"
            "Comedores" -> "Comedores:\n- Opción 1\n- Opción 2"
            "Localización" -> "Localización:\n- Opción 1"
            "Mi perfil" -> "Detalles de tu perfil."
            "Novedades" -> "Últimas noticias y eventos."
            "Información" -> "Información general del sistema."
            else -> "No hay resumen disponible."
        }

        summaryText.visibility = View.VISIBLE
        summaryText.text = summary
    }


    private fun navigateToOption(option: String) {
        Log.d("Option2", option)
        when (option) {
            "Mi perfil" -> Toast.makeText(this, "Opción no implementada", Toast.LENGTH_SHORT).show()
            "Gestión" -> startActivity(Intent(this, GestionActivity::class.java))
            "Comedores" -> Toast.makeText(this, "Opción no implementada", Toast.LENGTH_SHORT).show()
            "Novedades" -> Toast.makeText(this, "Opción no implementada", Toast.LENGTH_SHORT).show()
            "Localización" -> Toast.makeText(this, "Opción no implementada", Toast.LENGTH_SHORT).show()
            "Informacióm" -> Toast.makeText(this, "Opción no implementada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val handled = circularMenu.onTouchEvent(event)  // Pasa el evento a CircularMenuView
        return handled || super.dispatchTouchEvent(event)
    }

    private fun navigateToSelectedPage(summary: String) {
        Toast.makeText(this, "Ir a la página relacionada", Toast.LENGTH_SHORT).show()
    }
}


