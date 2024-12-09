package com.example.proyectonpi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyectonpi.ui.vistas.CircularMenuView
import com.example.proyectonpi.view.UpperBar

class Pantalla1 : BaseActivity() {

    private lateinit var circularMenu: CircularMenuView
    private lateinit var upperBar: UpperBar
    private lateinit var summaryText: TextView
    private lateinit var voiceSearchButton: ImageButton
    private lateinit var speechRecognizer: SpeechRecognizer
    private var speechIntent: Intent? = null

    private var REQUEST_RECORD_AUDIO_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla1_layout)

        circularMenu = findViewById(R.id.circularMenu)
        summaryText = findViewById(R.id.summaryText)
        voiceSearchButton = findViewById(R.id.voiceSearchButton)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent?.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent?.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)


        voiceSearchButton.setOnClickListener {
            Log.d("CircularMenuView", "Microfono tocado")
            checkMicrophonePermission()
        }

        circularMenu.setOnOptionSelectedListener(object : CircularMenuView.OnOptionSelectedListener {
            override fun onOptionSelected(option: String) {
                Log.d("CircularMenuView", option)
                updateTopOption(option)
                showSummary(option)
            }
        })

        // Set initial summary text (optional)
        summaryText.text = getString(R.string.desliza)

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

    }



    private fun checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        } else {
            startVoiceSearch()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceSearch()
            } else {
                Log.w("Pantalla1", "Microphone permission not granted")
            }
        }
    }

    private fun startVoiceSearch() {
        if (speechIntent == null) {
            speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechIntent?.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechIntent?.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        }
        speechRecognizer.setRecognitionListener(speechRecognitionListener)
        speechRecognizer.startListening(speechIntent)
    }

    private val speechRecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
            Log.d("SpeechRecognition", "Ready for speech")
        }
        override fun onBeginningOfSpeech() {
            TODO("Not yet implemented")
        }
        override fun onRmsChanged(p0: Float) {
            Log.d("SpeechRecognition", "RMS energy: $p0")
        }
        override fun onBufferReceived(p0: ByteArray?) {
            TODO("Not yet implemented")
        }
        override fun onEndOfSpeech() {
            TODO("Not yet implemented")
        }
        override fun onError(p0: Int) {
            Log.d("SpeechRecognition", "Error: $p0")
        }
        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches != null && matches.isNotEmpty()) {
                val text = matches[0]
                handleVoiceCommand(text)
            }
        }
        override fun onPartialResults(p0: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            TODO("Not yet implemented")
        }
    }
    fun handleVoiceCommand(text: String) {
        val selectedOption = circularMenu.options.find { it.name == text }
        if (selectedOption != null) {
            updateTopOption(text)
            showSummary(text)
            // Perform action based on the selected submenu (optional)
        } else {
            Log.w("Pantalla1", "Option not found: $text")
        }
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
        // Asegúrate de que el CircularMenu maneje el evento
        val handled = circularMenu.onTouchEvent(event)

        // Ahora pasa el evento a la vista superior (supervisor)
        return handled || super.dispatchTouchEvent(event)
    }
}


