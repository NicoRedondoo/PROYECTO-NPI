package com.example.proyectonpi.ui.vistas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    // Usar MutableLiveData para la lista de mensajes
    private val _messagesList = MutableLiveData<List<MessageModel>>()
    val messagesList: LiveData<List<MessageModel>> get() = _messagesList

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyBLJPBIG9x_BJYp-2xJCvbJuVvQ5--L6rQ"
    )

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
