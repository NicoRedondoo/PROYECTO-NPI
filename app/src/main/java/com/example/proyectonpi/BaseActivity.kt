package com.example.proyectonpi

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
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
    private var deslizamientoDosDedos = false
    private var tripleTapCount = 0
    private var lastTapTime: Long = 0
    private var initialX = floatArrayOf(0f, 0f) // Array para las posiciones en el eje x iniciales de ambos dedos
    private var initialY = floatArrayOf(0f, 0f) // Array para las posiciones en el eje y iniciales de ambos dedos
    private val displacementThresholdY = 500f // Umbral de desplazamiento eje Y
    private val displacementThresholdX = 200f // Umbral de desplazamiento eje X
    private var isActivityStarted = false // Para evitar inicio múltiple de la actividad

    // Variables para el sensor de luz ambiental
    private var lightSensor: Sensor? = null
    private var isAutoBrightnessEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedLanguage() // Aplica el idioma antes de cualquier configuración
        super.onCreate(savedInstanceState)

        // Inicializa el sensor de acelerómetro
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Inicializa el sensor de luz ambiental
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Listener para los toques consecutivos y deslizamientos
        val touchListener = object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            Log.d("BaseActivity", "ACTION_DOWN detectado")
                            val currentTime = System.currentTimeMillis()

                            // Detectar tres toques consecutivos
                            if (currentTime - lastTapTime < 300) { // 300 ms entre toques
                                tripleTapCount++
                            } else {
                                tripleTapCount = 1
                            }
                            lastTapTime = currentTime

                            if (tripleTapCount == 3) {
                                startActivity(Intent(this@BaseActivity, MainActivity::class.java))
                                tripleTapCount = 0
                            }
                            isActivityStarted = false // Restablecer

                        }

                        MotionEvent.ACTION_MOVE -> {
                            if (event.pointerCount == 2) {
                                if (!deslizamientoDosDedos){
                                    deslizamientoDosDedos = true

                                    // Posiciones iniciales de los dos dedos
                                    initialY[0] = event.getY(0)
                                    initialY[1] = event.getY(1)
                                    initialX[0] = event.getX(0)
                                    initialX[1] = event.getX(1)
                                    Log.d("BaseActivity", "Posiciones iniciales Y: d1 = ${initialY[0]}, d2 = ${initialY[1]}")
                                }
                                val firstDisplacementY = event.getY(0) - initialY[0]
                                val secondDisplacementY = event.getY(1) - initialY[1]
                                val firstDisplacementX = event.getX(0) - initialX[0]
                                val secondDisplacementX = event.getX(1) - initialX[1]
                                Log.d("BaseActivity", "DESPLAZAMIENTOS: d1 = ${firstDisplacementX}, d2 = ${secondDisplacementX}")

                                // Verificar el desplazamiento de los dos dedos
                                if (!isActivityStarted && firstDisplacementY > displacementThresholdY && secondDisplacementY > displacementThresholdY && firstDisplacementX < displacementThresholdX && secondDisplacementX < displacementThresholdX) {
                                    isActivityStarted = true
                                    startActivity(Intent(this@BaseActivity, HelpActivity::class.java))
                                    return true
                                }
                            }
                        }

                        MotionEvent.ACTION_UP -> {
                            Log.d("BaseActivity", "ACTION_UP detectado")
                            deslizamientoDosDedos = false
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
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Detener el listener del sensor
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        // Si el evento proviene del sensor de luz
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            val lightLevel = event.values[0] // Obtener el nivel de luz en lux
            if (isAutoBrightnessEnabled) {
                adjustScreenBrightness(lightLevel)
            }
        }

        // Si el evento proviene del sensor de acelerómetro (sacudida)
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val accelerationMagnitude = sqrt(x * x + y * y + z * z)

            val currentTime = System.currentTimeMillis()
            if (accelerationMagnitude > shakeThreshold && (currentTime - lastShakeTime > shakeCooldown)) {
                lastShakeTime = currentTime
                isAutoBrightnessEnabled = !isAutoBrightnessEnabled
                if (isAutoBrightnessEnabled){
                    Toast.makeText(this@BaseActivity, "Brillo automático activado", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@BaseActivity, "Brillo automático desactivado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }

    // Método para ajustar el brillo de la pantalla
    private fun adjustScreenBrightness(lightLevel: Float) {
        val brightness = when {
            lightLevel < 10 -> 0.1f // Brillo bajo
            lightLevel in 10.0..50.0 -> 0.5f // Brillo medio
            else -> 1.0f // Brillo máximo
        }

        // Ajustar el brillo de la pantalla
        val layoutParams = window.attributes
        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams
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
