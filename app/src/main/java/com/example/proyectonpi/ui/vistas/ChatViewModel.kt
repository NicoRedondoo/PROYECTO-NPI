package com.example.proyectonpi.ui.vistas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch
import java.text.Normalizer

class ChatViewModel : ViewModel() {

    // Usar MutableLiveData para la lista de mensajes
    private val _messagesList = MutableLiveData<List<MessageModel>>()
    val messagesList: LiveData<List<MessageModel>> get() = _messagesList

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyBLJPBIG9x_BJYp-2xJCvbJuVvQ5--L6rQ"
    )

    // Mapa de palabras clave y respuestas
    private val keywordResponses = mapOf(
        "Horario" to "La información sobre Tu Horario está en la pantalla \"Mi Perfil\"",
        "Expediente" to "La información sobre Expediente está en la pantalla \"Mi Perfil\"",
        "Matrícula" to "La información sobre Matrícula está en la pantalla \"Mi Perfil\"",
        "Profesores" to "La información sobre Profesores está en la pantalla \"Mi Perfil\"",
        "Prado" to "La información sobre Prado está en la pantalla \"Mi Perfil\"",
        "SWAD" to "La información sobre SWAD está en la pantalla \"Mi Perfil\"",
        "saldo" to "La información sobre saldo está en la pantalla \"Mi Perfil\"",
        "credibus" to "La información sobre credibus está en la pantalla \"Mi Perfil\"",
        "Biblioteca" to "La información sobre Biblioteca está en la pantalla \"Información\"",
        "Calendario" to "La información sobre Calendario está en la pantalla \"Información\"",
        "Tutorías" to "La información sobre Tutorias está en la pantalla \"Información\"",
        "Horario de" to "La información sobre Horarios de profesores está en la pantalla \"Información\"",
        "Horario del profesor" to "La información sobre Horarios de profesores está en la pantalla \"Información\"",
        "Cita" to "La información sobre Citas en secretaría está en la pantalla \"Gestión\"",
        "Secretaría" to "La información sobre Secretaría está en la pantalla \"Gestión\"",
        "TUI" to "La información sobre TUI está en la pantalla \"Gestión\"",
        "Foto" to "La información sobre Tu foto UGR está en la pantalla \"Gestión\"",
        "Resguardo" to "La información sobre Resguardo de tu matrícula está en la pantalla \"Gestión\"",
        "Becas" to "La información sobre Becas está en la pantalla \"Gestión\"",
        "Titulo" to "La información sobre Títulos en secretaría está en la pantalla \"Gestión\"",
        "Pagos" to "La información sobre pagos está en la pantalla \"Gestión\"",
        "Menú" to "La información sobre Menús está en la pantalla \"Comedores\"",
        "Telegram" to "La información sobre Telegram está en la pantalla \"Comedores\"",
        "Reserva" to "La información sobre Reservas de menús está en la pantalla \"Comedores\"",
        "Noticias" to "La información sobre Noticias está en la pantalla \"Novedades\"",
        "Novedades" to "La información sobre Noticias está en la pantalla \"Novedades\"",
        "Como llego a" to "Accede a la pantalla \"Localización\" para encontrar lo que buscas",
        "Aula" to "Accede a la pantalla \"Localización\" para encontrar lo que buscas",
        "Lugar" to "Accede a la pantalla \"Localización\" para encontrar lo que buscas",
        "Sitio" to "Accede a la pantalla \"Localización\" para encontrar lo que buscas",
        "Marcelino" to "¿Estás hablando de Marcelino? ¡Mi profesor favorito, el mejor de la ETSIIT!"

    )

    // Función para eliminar tildes y normalizar texto
    private fun String.removeAccents(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    }


    fun sendMessage(question: String) {
        viewModelScope.launch {
            // Accede a la lista actual de mensajes desde el LiveData
            val currentMessages = _messagesList.value?.toMutableList() ?: mutableListOf()

            // Construye el historial para el modelo Generative AI
            val history = currentMessages.map {
                content(it.role) {
                    text(it.message)
                }
            }

            // Inicia el chat con el historial
            val chat = generativeModel.startChat(history)

            // Normalizar la pregunta del usuario
            val normalizedQuestion = question.removeAccents().lowercase()

            // Buscar si el mensaje contiene alguna palabra clave
            val keywordMatch = keywordResponses.keys.find { keyword ->
                normalizedQuestion.contains(keyword.removeAccents().lowercase()) // Normalizar claves
            }

            // Si se encuentra una palabra clave, responder con el texto asociado
            if (keywordMatch != null) {
                currentMessages.add(MessageModel(question, "user"))
                currentMessages.add(MessageModel(keywordResponses[keywordMatch] ?: "", "model"))
                _messagesList.value = currentMessages
                return@launch
            }

            // Agrega el mensaje del usuario
            currentMessages.add(MessageModel(question, "user"))
            currentMessages.add(MessageModel("Typing...", "model"))

            // Actualiza la lista de mensajes
            _messagesList.value = currentMessages

            // Obtiene la respuesta del modelo
            val response = chat.sendMessage(question)
            currentMessages.removeAt(currentMessages.size - 1)

            // Agrega la respuesta del modelo
            currentMessages.add(MessageModel(response.text.toString(), "model"))

            // Actualiza la lista de mensajes nuevamente
            _messagesList.value = currentMessages

            // Logs para depuración
            Log.d("ChatView", "User dice: ${question}")
            Log.d("ChatView", "Gemini dice: ${response.text}")
        }
    }
}
