package com.example.proyectonpi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectonpi.ui.theme.PROYECTONPITheme

class GestionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion) // Vincula el archivo XML
//        setContent {
//            PROYECTONPITheme {
//                MainContent()
//            }
//        }
    }
}

//@Composable
//fun MainContent() {
//    // Usamos un Box como contenedor principal
//    Box(
//        modifier = Modifier
//            .fillMaxSize() // Ocupa todo el tamaño disponible
//    ) {
//        Greeting(
//            name = "Android",
//            //modifier = Modifier.padding(16.dp) // Agregamos un padding opcional
//        )
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    PROYECTONPITheme {
//        MainContent()
//    }
//}
