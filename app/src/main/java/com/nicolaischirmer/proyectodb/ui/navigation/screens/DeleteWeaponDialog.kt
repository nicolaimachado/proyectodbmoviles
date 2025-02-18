package com.munozcastrovirginia.proyectoapi.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteWeaponDialog(onConfirmDelete: () -> Unit, onDismiss: () -> Unit){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete weapon") },
        text = { Text("¿100% SEGURO?") },
        confirmButton = {
            Button(
                onClick = onConfirmDelete
            ) {
                Text("ACCEPT")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("CANCEL")
            }
        }
    )
}