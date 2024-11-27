package com.example.proyectonpi.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Nullable
import com.example.proyectonpi.R
import androidx.appcompat.app.AlertDialog
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import com.example.proyectonpi.HelpActivity


class UpperBar @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var isLoggedIn = false // Indica si la sesión está iniciada
    private var username: String? = null // Almacena el nombre de usuario

    init {
        LayoutInflater.from(context).inflate(R.layout.upper_toolbar, this, true)
        setupListeners()
    }

    private fun setupListeners() {
        val leftButton = findViewById<ImageButton>(R.id.left_button)
        leftButton.setOnClickListener {
            // Abrir la actividad de ayuda cuando se haga clic en el botón izquierdo
            val intent = Intent(context, HelpActivity::class.java)
            context.startActivity(intent)
        }

        findViewById<ImageButton>(R.id.right_button).setOnClickListener {
            if (isLoggedIn) {
                showLoggedInDialog()
            } else {
                showLoginDialog()
            }
        }
    }

    private fun showLoginDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val usernameField = dialogView.findViewById<EditText>(R.id.username_field)
        val passwordField = dialogView.findViewById<EditText>(R.id.password_field)
        val loginButton = dialogView.findViewById<Button>(R.id.login_button)
        val signupButton = dialogView.findViewById<Button>(R.id.signup_button)

        loginButton.setOnClickListener {
            val user = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (user.isNotBlank() && password.isNotBlank()) {
                isLoggedIn = true
                username = user
                dialog.dismiss()
                Toast.makeText(context, "Sesión iniciada como $user", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        signupButton.setOnClickListener {
            Toast.makeText(context, "Redirigiendo a creación de cuenta...", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    private fun showLoggedInDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_logged_in, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val usernameDisplay = dialogView.findViewById<TextView>(R.id.username_display)
        val logoutButton = dialogView.findViewById<Button>(R.id.logout_button)

        usernameDisplay.text = "Hola $username!"

        logoutButton.setOnClickListener {
            isLoggedIn = false
            username = null
            dialog.dismiss()
            Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}

