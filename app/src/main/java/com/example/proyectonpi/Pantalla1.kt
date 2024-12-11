package com.example.proyectonpi
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.font.Typeface
import com.example.proyectonpi.ui.vistas.CircularMenuView

class Pantalla1 : BaseActivity() {

    private lateinit var circularMenu: CircularMenuView
    private lateinit var summaryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla1_layout)

        circularMenu = findViewById(R.id.circularMenu)
        summaryText = findViewById(R.id.summaryText)

        circularMenu.setOnOptionSelectedListener(object : CircularMenuView.OnOptionSelectedListener {
            override fun onOptionSelected(option: String) {
                Log.d("CircularMenuView", option)
                updateTopOption(option)
                showSummary(option)
            }
        })

        // Set initial summary text (optional)
        summaryText.text = getString(R.string.desliza)

    }

    private fun updateTopOption(option: String) {
        summaryText.text = "Opción superior: $option"
        Log.d("CircularMenuView", "Opción superior: $option")
    }

    private fun showSummary(option: String) {
        val selectedOption = circularMenu.options.find { it.name == option }
        if (selectedOption != null) {
            val boldSpan = SpannableString(selectedOption.name)
            boldSpan.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, boldSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            val summary = SpannableStringBuilder()
            summary.append(boldSpan)
            // Add line break using "\n" or System.getProperty("line.separator")
            summary.append("\n") // This is the line break
            summary.append("\n")
            summary.append("\n")
            summary.append(selectedOption.description)

            summaryText.text = summary

            summaryText.text = summary
        } else {
            Log.w("Pantalla1", "Option not found: $option")
            summaryText.visibility = View.GONE  // Hide if option not found
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val handled = circularMenu.onTouchEvent(event)
        return handled || super.dispatchTouchEvent(event)
    }

}


