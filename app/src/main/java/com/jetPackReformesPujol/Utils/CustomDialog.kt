package com.jetPackReformesPujol.materiales.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.format.TextStyle

@Composable
fun CustomDialog(
    show: Boolean,
    value: String,
    titulo: String,
    textButton: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var txtField by remember { mutableStateOf(value) }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .size(450.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                    value = txtField,
                    onValueChange = { txtField = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)

                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onConfirm(txtField)
                        txtField = ""
                    }
                ) {
                    Text(text = textButton)
                }
            }
        }
    }
}