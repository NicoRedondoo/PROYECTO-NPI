package com.example.proyectonpi

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.math.sqrt

open class BaseActivity : AppCompatActivity(), SensorEventListener {
    // Variables para el sensor de acelerómetro
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var shakeThreshold = 50.0f // Umbral de sacudida
    private var lastShakeTime: Long = 0
    private val shakeCooldown = 1000 // Tiempo mínimo entre detecciones de sacudida (en ms)

    // Variables para el manejo de toques y deslizamientos
    private var tripleTapCount = 0
    private var lastTapTime: Long = 0
    private var initialY = floatArrayOf(0f, 0f) // Array para las posiciones iniciales de ambos dedos
    private val displacementThreshold = 1000f // Umbral de desplazamiento
    private var isActivityStarted = false // Evitar inicio múltiple de la actividad

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedLanguage() // Aplica el idioma antes de cualquier configuración
        super.onCreate(savedInstanceState)

        // Inicializa el sensor de acelerómetro
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Listener para los toques consecutivos y deslizamientos
        val touchListener = object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            val currentTime = System.currentTimeMillis()

                            // Detectar tres toques consecutivos
                            if (currentTime - lastTapTime < 300) { // 300 ms entre toques
                                tripleTapCount++
                            } else {
                                tripleTapCount = 1
                            }
                            lastTapTime = currentTime

                            if (tripleTapCount == 3) {
                                startActivity(Intent(this@BaseActivity, Pantalla1::class.java))
                                tripleTapCount = 0
                            }

                            // Guardar las posiciones iniciales de los dedos
                            if (event.pointerCount >= 2) {
                                initialY[0] = event.getY(0)
                                initialY[1] = event.getY(1)
                            }
                        }

                        MotionEvent.ACTION_MOVE -> {
                            if (event.pointerCount == 2) {
                                val firstDisplacement = event.getY(0) - initialY[0]
                                val secondDisplacement = event.getY(1) - initialY[1]

                                // Verificar si ambos dedos se desplazaron más allá del umbral
                                if (!isActivityStarted && firstDisplacement > displacementThreshold && secondDisplacement > displacementThreshold) {
                                    isActivityStarted = true
                                    startActivity(Intent(this@BaseActivity, HelpActivity::class.java))
                                    // Reseteamos las posiciones iniciales después de detectar el movimiento
                                    initialY[0] = 0f
                                    initialY[1] = 0f
                                    return true
                                }
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            // Resetear las posiciones iniciales cuando se levanten los dedos
                            initialY[0] = 0f
                            initialY[1] = 0f
                            isActivityStarted = false // Restablecer el estado después de soltar los dedos
                        }
                    }
                }
                return true
            }
        }

        // Aplicar el listener al layout de la actividad
        val layout = findViewById<View>(android.R.id.content)
        layout.setOnTouchListener(touchListener)
    }

    override fun onResume() {
        super.onResume()
        // Registra el listener del sensor
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Detiene el listener del sensor para ahorrar batería
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        // Calcula la magnitud del vector de aceleración
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val accelerationMagnitude = sqrt(x * x + y * y + z * z)

        val currentTime = System.currentTimeMillis()
        if (accelerationMagnitude > shakeThreshold && (currentTime - lastShakeTime > shakeCooldown)) {
            lastShakeTime = currentTime
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }

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
}
