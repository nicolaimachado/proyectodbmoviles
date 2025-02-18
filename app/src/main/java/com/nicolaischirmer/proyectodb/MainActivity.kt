package com.nicolaischirmer.proyectodb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nicolaischirmer.proyectodb.firebase.AuthManager
import com.nicolaischirmer.proyectodb.ui.navigation.Navigation
import com.nicolaischirmer.proyectodb.ui.theme.ProyectodbTheme

class MainActivity : ComponentActivity() {
    val auth = AuthManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ProyectodbTheme {
                Navigation(auth)
            }
        }
    }
}
