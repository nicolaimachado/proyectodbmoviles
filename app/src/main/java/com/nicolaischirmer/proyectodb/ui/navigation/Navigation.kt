package com.nicolaischirmer.proyectodb.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nicolaischirmer.proyectodb.firebase.AuthManager
import com.nicolaischirmer.proyectodb.firebase.FirestoreManager
import com.nicolaischirmer.proyectodb.ui.navigation.screens.ForgotPasswordScreen
import com.nicolaischirmer.proyectodb.ui.navigation.screens.LoginScreen
import com.nicolaischirmer.proyectodb.ui.navigation.screens.ScreenInicio
import com.nicolaischirmer.proyectodb.ui.navigation.screens.SignUpScreen

@Composable
fun Navigation(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val firestore = FirestoreManager(auth, context)

    NavHost(
        navController = navController,
        startDestination = login
    ) {
        composable<login> {
            LoginScreen(
                auth,
                { navController.navigate("signUp") },
                {
                    navController.navigate("screenInicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                { navController.navigate("forgotPassword") }
            )
        }
        composable<signUp> {
            SignUpScreen(
                auth
            ) { navController.popBackStack() }
        }

        composable<forgotPassword> {
            ForgotPasswordScreen(
                auth
            ) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        composable<screenInicio> {
            ScreenInicio(
                auth,
                firestore,
                {
                    navController.navigate("login") {
                        popUpTo("screenInicio") { inclusive = true }
                    }
                }
            )
        }
    }
}