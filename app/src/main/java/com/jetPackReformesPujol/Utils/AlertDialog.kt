package com.jetPackReformesPujol.Utils

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialog(showAlert: Boolean,onDismiss: () -> Unit, onConfirm: () -> Unit, titulo: String, descripcion: String) {
    Column {
        if (showAlert) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = titulo)
                },
                text = {
                    Text(descripcion)
                },
                confirmButton = {
                    TextButton(
                        onClick = onConfirm ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDismiss ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}