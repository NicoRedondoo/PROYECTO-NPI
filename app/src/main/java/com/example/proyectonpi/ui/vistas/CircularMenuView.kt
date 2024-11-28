package com.example.proyectonpi.ui.vistas

import android.util.Log

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.proyectonpi.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

// Definir la clase MenuOption
data class MenuOption(
    val name: String,
    val imageResId: Int, // ID de la imagen del recurso
    var angle: Float // Ángulo de la opción en el círculo
)

class CircularMenuView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    interface OnOptionSelectedListener {
        fun onOptionSelected(option: String)
    }

    interface OnTopItemChangeListener {
        fun onTopItemChanged(option: String)
    }

    private var onTopItemChangeListener: OnTopItemChangeListener? = null

    fun setOnTopItemChangeListener(listener: OnTopItemChangeListener) {
        onTopItemChangeListener = listener
    }

    private var onOptionSelectedListener: OnOptionSelectedListener? = null

    fun setOnOptionSelectedListener(listener: OnOptionSelectedListener) {
        onOptionSelectedListener = listener
    }
    private val options = listOf(
        MenuOption("Mi perfil", R.drawable.miperfil, 0f),
        MenuOption("Información", R.drawable.info, 60f),
        MenuOption("Gestión", R.drawable.gestion, 120f),
        MenuOption("Comedores", R.drawable.comedor, 180f),
        MenuOption("Novedades", R.drawable.novedades, 240f),
        MenuOption("Localización", R.drawable.localizacion, 300f)
    )
    private val optionBitmaps = mutableMapOf<Int, Bitmap>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    private val colors = listOf(
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA
    )

    private var rotationAngle = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var innerRadius = 350f  // Radio interno (corona)
    private var outerRadius = 600f  // Radio externo (corona)

    private var initialAngle = 0f
    private var swipeThreshold = 200


    private val outerRect = RectF()
    private val innerRect = RectF()

    private var whiteCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Carga los íconos solo una vez, al redimensionar
        options.forEach { option ->
            val bitmap = BitmapFactory.decodeResource(resources, option.imageResId)
            optionBitmaps[option.imageResId] = bitmap
        }

        centerX = (w / 2).toFloat()
        centerY = h.toFloat() // Colocar en la parte inferior
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dibujar el círculo blanco en el centro
        canvas.drawCircle(centerX, centerY, innerRadius, whiteCirclePaint)

        val sweepAngle = 360f / options.size  // Cambiado para ser dinámico según el número de opciones
        var startAngle = rotationAngle

        // Dibujar las opciones en el anillo de la corona
        for (i in options.indices) {
            // Usamos el color de la opción
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

            // Usar PorterDuff.Mode.CLEAR para "borrar" el interior y crear la forma de anillo
            paint.color = Color.WHITE // Establecemos el color del pincel a blanco
            canvas.drawArc(innerRect, startAngle, sweepAngle, true, paint)

            // Restaurar el estado del canvas
            canvas.restore()

            // Dibujar los iconos en el anillo entre los radios
            val iconAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
            val iconX = centerX + ((innerRadius + (outerRadius - innerRadius) * 0.5) * cos(iconAngle)).toFloat()
            val iconY = centerY + ((innerRadius + (outerRadius - innerRadius) * 0.5) * sin(iconAngle)).toFloat()

            // Redimensionar el icono a 20px
            val iconSize = 200

            // Dibujar el icono en el ángulo correcto
            val iconBitmap = BitmapFactory.decodeResource(resources, options[i].imageResId)
            val iconRect = Rect(
                (iconX - iconSize / 2).toInt(),
                (iconY - iconSize / 2).toInt(),
                (iconX + iconSize / 2).toInt(),
                (iconY + iconSize / 2).toInt()
            )
            canvas.drawBitmap(iconBitmap, null, iconRect, null)

            // Dibujar el texto en el anillo
            //val textAngle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
            //val textX = centerX + ((innerRadius + (outerRadius - innerRadius) * 0.5) * cos(textAngle)).toFloat()
            //val textY = centerY + ((innerRadius + (outerRadius - innerRadius) * 0.5) * sin(textAngle)).toFloat()
            //canvas.drawText(options[i].name, textX, textY, textPaint)

            startAngle += sweepAngle
        }

        // Mostrar el nombre del item en la parte superior de la rueda
        val selectedOptionIndex = getSelectedOption()
        val selectedOption = options[selectedOptionIndex]

        // Dibujar el nombre de la opción seleccionada en la parte superior
        val nameTextX = centerX
        val nameTextY = centerY - outerRadius - 30f // Ajuste de la posición Y para que se dibuje arriba
        canvas.drawText(selectedOption.name, nameTextX, nameTextY, textPaint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Verificar si el toque está dentro del área de la rueda
        val distanceFromCenter = Math.sqrt(
            Math.pow(event.x - centerX.toDouble(), 2.0) + Math.pow(event.y - centerY.toDouble(), 2.0)
        )
        if (distanceFromCenter < outerRadius) {
            // Solo manejar el toque si está dentro de la rueda
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialAngle = event.x // Guardar posición inicial
                }
                MotionEvent.ACTION_MOVE -> {
                    // Detectar el deslizamiento
                    val deltaX = event.x - initialAngle
                    if (abs(deltaX) > swipeThreshold) {
                        if (deltaX > 0) {
                            // Deslizar a la derecha (rotar en sentido horario)
                            rotationAngle += 60f
                        } else {
                            // Deslizar a la izquierda (rotar en sentido antihorario)
                            rotationAngle -= 60f
                        }
                        initialAngle = event.x // Actualizar el ángulo inicial
                        invalidate()  // Redibujar la vista

                        // Detectar el ítem superior actual
                        val selectedOptionIndex = getSelectedOption()
                        val selectedOption = options[selectedOptionIndex]

                        // Notificar el cambio del ítem superior
                        onTopItemChangeListener?.onTopItemChanged(selectedOption.name)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    val selectedOptionIndex = getSelectedOption()
                    val selectedOption = options[selectedOptionIndex]

                    // Imprimir el ángulo de selección y la opción seleccionada
                    Log.d("CircularMenuView", "Ángulo seleccionado: $rotationAngle°")
                    Log.d("CircularMenuView", "Opción seleccionada: $selectedOption")
                    onOptionSelectedListener?.onOptionSelected(selectedOption.name)
                }
            }
            return true // Si el toque fue manejado por la rueda, devolver true
        }

        return super.onTouchEvent(event) // Si no fue un toque en la rueda, delegar el evento a otras vistas (botones)
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

}
