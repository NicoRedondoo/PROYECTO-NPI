package com.example.proyectonpi.ui.vistas

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.proyectonpi.ComedorActivity
import com.example.proyectonpi.GestionActivity
import com.example.proyectonpi.InformacionActivity
import com.example.proyectonpi.LocalizacionActivity
import com.example.proyectonpi.MiPerfilActivity
import com.example.proyectonpi.NovedadesActivity
import com.example.proyectonpi.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

// Definir la clase MenuOption
data class MenuOption(
    val name: String,
    val imageResId: Int, // ID de la imagen del recurso
    var angle: Float, // Ángulo de la opción en el círculo
    val description: String // Add description field
)

class CircularMenuView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var gestureDetector: GestureDetector
    private var isSweeping = false

    init {
        val gestureListener = object : GestureDetector.OnGestureListener {

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onShowPress(e: MotionEvent) {}

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.d("CircularMenuView", "Toque simple confirmado")
                val selectedOptionIndex = getSelectedOption()
                val selectedOption = options[selectedOptionIndex]
                onOptionSelectedListener?.onOptionSelected(selectedOption.name)
                openSubmenu(selectedOption.name)
                return true
            }

            override fun onScroll(
                p0: MotionEvent?,
                e1: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                // Solo detectar desplazamientos horizontales significativos
                if (!isSweeping && abs(distanceX) > swipeThreshold) { // Si el desplazamiento es mayor que el umbral
                    isSweeping = true
                    if (distanceX > 0) {
                        // Desplazamiento hacia la derecha
                        rotationAngle += 60f
                        Log.d("CircularMenuView", "Desplazamiento a la derecha detectado")
                    } else {
                        // Desplazamiento hacia la izquierda
                        rotationAngle -= 60f
                        Log.d("CircularMenuView", "Desplazamiento a la izquierda detectado")
                    }

                    rotationAngle = (rotationAngle % 360 + 360) % 360 // Normalizar el ángulo
                    // Notificar el cambio de opción seleccionada
                    val selectedOptionIndex = getSelectedOption()
                    val selectedOption = options[selectedOptionIndex]
                    Log.d("CircularMenuView", "Opción seleccionada: $selectedOption")
                    onOptionSelectedListener?.onOptionSelected(selectedOption.name)

                    invalidate() // Redibujar para reflejar el cambio
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {}

            override fun onFling(
                p0: MotionEvent?,
                e1: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                return true
            }
        }
        gestureDetector = GestureDetector(context, gestureListener)
    }

    interface OnOptionSelectedListener {
        fun onOptionSelected(option: String)
    }

    private var onOptionSelectedListener: OnOptionSelectedListener? = null


    fun setOnOptionSelectedListener(listener: OnOptionSelectedListener) {
        onOptionSelectedListener = listener
    }

    val titulo_perfil = context.getString(R.string.miperfil)
    val titulo_informacion = context.getString(R.string.informacion)
    val titulo_gestion = context.getString(R.string.gestion)
    val titulo_comedores = context.getString(R.string.comedores)
    val titulo_novedades = context.getString(R.string.novedades)
    val titulo_localizacion = context.getString(R.string.localizacion)

    val descripcion_perfil = context.getString(R.string.descripcion_miperfil)
    val descripcion_informacion = context.getString(R.string.descripcion_informacion)
    val descripcion_gestion = context.getString(R.string.descripcion_gestion)
    val descripcion_comedores = context.getString(R.string.descripcion_comedores)
    val descripcion_novedades = context.getString(R.string.descripcion_comedores)
    val descripcion_localizacion = context.getString(R.string.descripcion_localizacion)
    val options = listOf(
        MenuOption(titulo_novedades, R.drawable.usuario, 0f, descripcion_novedades),
        MenuOption(titulo_informacion, R.drawable.comedor_2, 60f, descripcion_comedores),
        MenuOption(titulo_gestion, R.drawable.gestion_1, 120f, descripcion_gestion),
        MenuOption(titulo_comedores, R.drawable.informacion_1, 180f, descripcion_informacion),
        MenuOption(titulo_perfil, R.drawable.mensaje_recibido, 240f, descripcion_perfil),
        MenuOption(titulo_localizacion, R.drawable.localizacion1, 300f, descripcion_localizacion)
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    private var rotationAngle = 0f

    private val colors = listOf(
        Color.rgb(255, 77, 77), Color.rgb(204, 62, 62),Color.rgb(255, 77, 77), Color.rgb(204, 62, 62),Color.rgb(255, 77, 77), Color.rgb(204, 62, 62)
    )
    private var centerX = 0f
    private var centerY = 0f
    private var innerRadius = 350f  // Radio interno (corona)
    private var outerRadius = 600f  // Radio externo (corona)

    private var swipeThreshold = 60

    private val outerRect = RectF()
    private val innerRect = RectF()

    private var whiteCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = (w / 2).toFloat()
        centerY = h.toFloat() // Colocar en la parte inferior
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dibujar el círculo blanco en el centro
        canvas.drawCircle(centerX, centerY, innerRadius, whiteCirclePaint)

        val sweepAngle = 360f / options.size
        var startAngle = rotationAngle

        for (i in options.indices) {

            // Calcular el ángulo central del segmento
            paint.color = colors[i]

            outerRect.set(
                centerX - outerRadius, centerY - outerRadius,
                centerX + outerRadius, centerY + outerRadius
            )
            innerRect.set(
                centerX - innerRadius, centerY - innerRadius,
                centerX + innerRadius, centerY + innerRadius
            )

            canvas.save()
            canvas.drawArc(outerRect, startAngle, sweepAngle, true, paint)

            paint.color = Color.WHITE
            canvas.drawArc(innerRect, startAngle, sweepAngle, true, paint)
            canvas.restore()

            val iconAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
            val iconX = centerX + ((innerRadius + (outerRadius - innerRadius) * 0.5) * cos(iconAngle)).toFloat()
            val iconY = centerY + ((innerRadius + (outerRadius - innerRadius) * 0.5) * sin(iconAngle)).toFloat()

            val iconSize = 200

            val iconBitmap = BitmapFactory.decodeResource(resources, options[i].imageResId)
            val iconRect = Rect(
                (iconX - iconSize / 2).toInt(),
                (iconY - iconSize / 2).toInt(),
                (iconX + iconSize / 2).toInt(),
                (iconY + iconSize / 2).toInt()
            )
            canvas.drawBitmap(iconBitmap, null, iconRect, null)

            startAngle += sweepAngle
        }

        // Actualizar el nombre de la opción seleccionada
        val selectedOptionIndex = getSelectedOption()
        val selectedOption = options[selectedOptionIndex]

        val nameTextX = centerX
        val nameTextY = centerY - outerRadius - 30f
        canvas.drawText(selectedOption.name, nameTextX, nameTextY, textPaint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        // Comprobar si el toque está dentro del área del CircularMenuView
        if (!hitTest(touchX, touchY)) {
            return false // Si el toque está fuera, no lo manejamos
        }

        // Si el toque está dentro, delegamos el evento al GestureDetector
        val result = gestureDetector.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            isSweeping = false
        }
        return result
    }

    // Función para comprobar si un punto está dentro del área del CircularMenuView
    private fun hitTest(x: Float, y: Float): Boolean {
        val distance = Math.sqrt(Math.pow((x - centerX).toDouble(), 2.0) + Math.pow((y - centerY).toDouble(), 2.0))
        return distance <= outerRadius
    }


    // Método para obtener la opción seleccionada
    private fun getSelectedOption(): Int {
        val sweepAngle = 360f / 6
        val normalizedAngle = (rotationAngle % 360 + 360) % 360
        // Ajustar el ángulo inicial (por ejemplo, sumar 30 grados)
        val adjustedAngle = normalizedAngle + 30f
        val closestIndex = ((adjustedAngle / sweepAngle).toInt()) % 6
        return closestIndex
    }

    fun openSubmenu(optionName: String) {
        val intent = when (optionName) {
            "Gestión" -> Intent(context, GestionActivity::class.java)
            "Mi Perfil" -> Intent(context, MiPerfilActivity::class.java)
            "Información" -> Intent(context, InformacionActivity::class.java)
            "Comedores" -> Intent(context, ComedorActivity::class.java)
            "Novedades" -> Intent(context, NovedadesActivity::class.java)
            "Localización" -> Intent(context, LocalizacionActivity::class.java)
            else -> null
        }
        intent?.let { context.startActivity(it) }
    }

}