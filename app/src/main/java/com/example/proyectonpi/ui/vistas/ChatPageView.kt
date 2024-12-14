package com.example.proyectonpi.ui.vistas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectonpi.R

class ChatActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        // Inicializar las vistas
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this).apply { stackFromEnd=true }

        // Configurar el adaptador para RecyclerView
        adapter = MessageAdapter(mutableListOf()) // Iniciar con lista vacía
        recyclerView.adapter = adapter

        // Observar cambios en la lista de mensajes
        viewModel.messagesList.observe(this, Observer { messages ->
            // Cuando los mensajes cambian, notificar al adaptador y actualizar la UI
            adapter.updateMessages(messages)
            recyclerView.scrollToPosition(messages.size - 1) // Desplazar hacia el último mensaje
        })

        // Referencias a los elementos de la UI
        val messageInput: EditText = findViewById(R.id.message_input)
        val sendButton: ImageButton = findViewById(R.id.send_button)

        // Configuración del botón enviar
        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                messageInput.text.clear() // Limpiar campo después de enviar
            }
        }
    }
}

class MessageAdapter(private var messages: List<MessageModel>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<MessageModel>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageContainer: LinearLayout = itemView.findViewById(R.id.message_container)
        private val messageText: TextView = itemView.findViewById(R.id.message_text)
        private val senderText: TextView = itemView.findViewById(R.id.sender_text)

        fun bind(message: MessageModel) {
            // Ajustar texto del mensaje y remitente
            messageText.text = message.message
            senderText.text = if (message.role == "user") "User" else "Model"

            // Ajustar alineación según remitente
            val params = messageContainer.layoutParams as LinearLayout.LayoutParams
            if (message.role == "user") {
                params.gravity = android.view.Gravity.END // Alineación derecha
                messageContainer.setBackgroundResource(R.drawable.message_background_user).apply {
                    View.TEXT_ALIGNMENT_TEXT_END
                }
            } else {
                params.gravity = android.view.Gravity.START // Alineación izquierda
                messageContainer.setBackgroundResource(R.drawable.message_background_model).apply {
                    View.TEXT_ALIGNMENT_TEXT_START
                }
            }
            messageContainer.layoutParams = params
        }
    }
}

