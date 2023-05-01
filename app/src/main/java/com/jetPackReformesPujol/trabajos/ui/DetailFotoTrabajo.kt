package com.jetPackReformesPujol.trabajos.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jetPackReformesPujol.Utils.MyTopAppBar
import timber.log.Timber

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContentDetailFotoTrabajo(
    photoId: String,
    navigateUp: () -> Unit,
    navigationController: NavHostController,
    trabajosViewModel: TrabajosViewModel
) {

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Reformes Pujol") },
            backgroundColor = Color.Blue,
            contentColor = Color.White,
            navigationIcon = {
                IconButton(onClick = { navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = {
                    trabajosViewModel.onShowDeleteAlertDialog()
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete image")
                }
            })
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            ShowImage(photoId)
            DeleteImageOfFBStorage(photoId, trabajosViewModel, navigationController)
        }
    }
}

@Composable
fun ShowImage(photoId: String) {
    if (!photoId.isNullOrEmpty()) {
        Timber.d("Valor actual de photoId: %s", photoId)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoId)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Timber.d("photoId es nulo o vacío")
            Text(text = "Photo ID: $photoId")
        }
    }
}

@Composable
fun DeleteImageOfFBStorage(
    photoId: String,
    trabajosViewModel: TrabajosViewModel,
    navigationController: NavHostController
) {

    val showDeleteAlertDialog: Boolean by trabajosViewModel.showDeleteDialog.observeAsState(false)
    val imagenEliminada: Boolean by trabajosViewModel.imagenEliminada.collectAsState(false)

    com.jetPackReformesPujol.Utils.AlertDialog(
        showAlert = showDeleteAlertDialog,
        onDismiss = { trabajosViewModel.dialogDeleteDismiss() },
        onConfirm = { trabajosViewModel.deletePictureToFBStorage(photoId) },
        titulo = "Desea eliminar la imagen de la base de datos?",
        descripcion = "Eliminará la imagen de la base de datos"
    )

    LaunchedEffect(imagenEliminada) {
        if (imagenEliminada) {
            navigationController.popBackStack()
        }
    }
}

