package com.example.proyectonpi.ui.vistas

import android.util.Log

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CircularMenuView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    interface OnOptionSelectedListener {
        fun onOptionSelected(option: String)
    }

    private var onOptionSelectedListener: OnOptionSelectedListener? = null

    fun setOnOptionSelectedListener(listener: OnOptionSelectedListener) {
        onOptionSelectedListener = listener
    }
    private val options = listOf("Mi perfil", "Información", "Gestión", "Comedores", "Novedades", "Localización")
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    private val colors = listOf(
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA
    )

    private var rotationAngle = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var innerRadius = 150f  // Radio interno (corona)
    private var outerRadius = 250f  // Radio externo (corona)
    private var initialAngle = 0f
    private var lastAngle = 0f

    private val outerRect = RectF()
    private val innerRect = RectF()

    private val clearmode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w / 2).toFloat()
        centerY = h - 200f  // Colocar en la parte inferior
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val sweepAngle = 360f / options.size
        var startAngle = rotationAngle

        // Dibujar las opciones en el anillo de la corona
        for (i in options.indices) {
            paint.color = colors[i]

            // Dibujar el arco exterior del anillo
            outerRect.set(
                centerX - outerRadius, centerY - outerRadius,
                centerX + outerRadius, centerY + outerRadius
            )
            innerRect.set(
                centerX - innerRadius, centerY - innerRadius,
                centerX + innerRadius, centerY + innerRadius
            )

            // Guardar el estado del canvas para recortar el centro
            canvas.save()

            // Dibujar el arco exterior
            canvas.drawArc(outerRect, startAngle, sweepAngle, true, paint)

            // Usar `PorterDuff.Mode.CLEAR` para "borrar" el interior y crear la forma de anillo
            paint.xfermode = clearmode
            canvas.drawArc(innerRect, startAngle, sweepAngle, true, paint)
            paint.xfermode = null

            // Restaurar el estado del canvas
            canvas.restore()

            // Dibujar el texto en el anillo
            val textAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
            val textX = centerX + ((innerRadius + (outerRadius - innerRadius) * 0.5) * cos(textAngle)).toFloat()
            val textY = centerY + ((innerRadius + (outerRadius - innerRadius) * 0.5) * sin(textAngle)).toFloat()
            canvas.drawText(options[i], textX, textY, textPaint)

            startAngle += sweepAngle
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Guardar el ángulo inicial cuando el dedo toca la pantalla
                initialAngle = getAngle(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                // Calcular el ángulo del nuevo toque
                val currentAngle = getAngle(event.x, event.y)
                // Actualizar el ángulo de rotación según el desplazamiento del dedo
                rotationAngle += currentAngle - initialAngle
                initialAngle = currentAngle
                invalidate()  // Redibujar la vista
            }
            MotionEvent.ACTION_UP -> {
                val selectedOptionIndex = getSelectedOption()
                val selectedOption = options[selectedOptionIndex]

                // Imprimir el ángulo de selección y la opción seleccionada
                val angle = getAngle(event.x, event.y)  // Obtiene el ángulo
                Log.d("CircularMenuView", "Ángulo seleccionado: $angle°")
                Log.d("CircularMenuView", "Opción seleccionada: $selectedOption")
                onOptionSelectedListener?.onOptionSelected(selectedOption)
            }
        }
        return true
    }

    private fun getAngle(x: Float, y: Float): Float {
        val dx = x - centerX
        val dy = y - centerY
        return Math.toDegrees(atan2(dy, dx).toDouble()).toFloat()
    }

    // Método para obtener la opción seleccionada
    private fun getSelectedOption(): Int {
        val sweepAngle = 360f / options.size
        val normalizedAngle = (rotationAngle - 90 + 360f) % 360f
        return ((normalizedAngle / sweepAngle).toInt()) % options.size
    }
}
