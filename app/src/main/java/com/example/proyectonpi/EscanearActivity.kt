package com.example.proyectonpi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

import android.widget.Toast
import android.widget.Button

class EscanearActivity : BaseActivity() {

    private var resultadoEscaneo: String? = null // Variable para guardar el resultado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_escanear)
        iniciarEscaneo() // Inicia el escaneo al abrir la actividad

    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            resultadoEscaneo = result.contents // Guarda el resultado del escaneo
            comprobarResultado() // Comprueba el resultado una vez que está disponible
        } else {
            // Maneja el caso de un escaneo cancelado si es necesario
            Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            finish() // Finaliza la actividad incluso si el escaneo es cancelado
        }
    }

    private fun iniciarEscaneo() {
        val options = ScanOptions()
        options.setPrompt("Escanea un código QR") // Mensaje para el usuario
        options.setBeepEnabled(true) // Habilitar sonido al escanear
        options.setOrientationLocked(true) // Bloquear la orientación
        options.setBarcodeImageEnabled(true) // Habilitar guardar imagen del código escaneado
        barcodeLauncher.launch(options)
    }

    private fun comprobarResultado() {
        if (resultadoEscaneo != null) {
            when (resultadoEscaneo) {
                "consultarSaldoCredibus" -> {
                    val intent = Intent(this, ConsultarSaldoActivity::class.java)
                    startActivity(intent)
                }
                "citaEnSecretaria" -> {
                    val intent = Intent(this, CitaSecretariaActivity::class.java)
                    startActivity(intent)
                }
                "solicitudGeneral" -> {
                    val intent = Intent(this, SolicitudGeneralActivity::class.java)
                    startActivity(intent)
                }
                "certificadoDeCalificaciones" -> {
                    val intent = Intent(this, CertificadoCalificacionesActivity::class.java)
                    startActivity(intent)
                }
                "certificadoDeMatricula" -> {
                    val intent = Intent(this, CertificadoDeMatriculaActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Código no reconocido", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
