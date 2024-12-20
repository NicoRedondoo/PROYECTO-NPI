package com.example.proyectonpi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectonpi.ui.theme.PROYECTONPITheme

class ComedorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comedor) // Vincula el archivo XML
        setupListeners()
//        setContent {
//            PROYECTONPITheme {
//                MainContent()
//            }
//        }
    }

    private fun setupListeners() {
        val menuSemanalButton = findViewById<Button>(R.id.btnMenuSemanal)
        menuSemanalButton.setOnClickListener {
            val intent = Intent(this, MenuSemanalActivity::class.java)
            startActivity(intent)
        }

        val botTelegramButton = findViewById<Button>(R.id.btnBotTelegram)
        botTelegramButton.setOnClickListener {
            val intent = Intent(this, BotTelegramActivity::class.java)
            startActivity(intent)
        }

        val reservaTuMenuButton = findViewById<Button>(R.id.btnReservaTuMenu)
        reservaTuMenuButton.setOnClickListener {
            val intent = Intent(this, ReservaTuMenuActivity::class.java)
            startActivity(intent)
        }

        val informacionSobreMenusButton = findViewById<Button>(R.id.btnInformacionSobreMenus)
        informacionSobreMenusButton.setOnClickListener {
            val intent = Intent(this, InformacionSobreMenus::class.java)
            startActivity(intent)
        }

    }
}