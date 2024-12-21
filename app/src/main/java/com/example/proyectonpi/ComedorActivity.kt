package com.example.proyectonpi

import android.content.Intent
import android.os.Bundle
import android.widget.Button

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

        val menuDelDiaButton = findViewById<Button>(R.id.btnMenuDelDia)
        menuDelDiaButton.setOnClickListener {
            val intent = Intent(this, MenuDelDiaActivity::class.java)
            startActivity(intent)
        }

        val menuParaLlevarButton = findViewById<Button>(R.id.btnMenuParaLlevar)
        menuParaLlevarButton.setOnClickListener {
            val intent = Intent(this, MenuParaLLevarActivity::class.java)
            startActivity(intent)
        }


    }
}