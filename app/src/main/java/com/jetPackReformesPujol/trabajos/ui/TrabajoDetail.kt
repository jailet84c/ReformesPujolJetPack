package com.jetPackReformesPujol.trabajos.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.FirebaseDatabase
import com.jetPackReformesPujol.trabajos.model.ClienteModelFirebase
import timber.log.Timber
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TrabajoDetail(
    selectedPhotoId: (String) -> Unit,
    clienteId: String,
    navigateUp: () -> Unit,
    navigationController: NavHostController,
    trabajosViewModel: TrabajosViewModel
) {

    val isLoading by trabajosViewModel.isLoading.observeAsState(true)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                trabajosViewModel.updateSelectedImageUri(uri)
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reformes Pujol") },
                backgroundColor = Blue,
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
                        Icon(Icons.Filled.Delete, contentDescription = "Delete client")
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                modifier = Modifier
                    .padding(16.dp)
                    .zIndex(1f),
                backgroundColor = Yellow,
            ) {
                Icon(
                    Icons.Filled.Add, contentDescription = "",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column {
                GetDetailsFromFBDatabase(clienteId)
                AddPicturesToFirebaseStorage(
                    clienteId,
                    trabajosViewModel
                )
                GetPicturesFromDBStorage(
                    isLoading,
                    selectedPhotoId,
                    clienteId,
                    trabajosViewModel
                )
                DeleteDataClientFirebase(clienteId, trabajosViewModel, navigationController)
            }
        }
    }
}

@Composable
fun AddPicturesToFirebaseStorage(
    clienteId: String,
    trabajosViewModel: TrabajosViewModel,
) {

    val selectedImageUri by trabajosViewModel.selectedImageUri.observeAsState(null)

    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let {
            trabajosViewModel.addPicturesFromStorageToFBStorage(clienteId)
        }
    }
}

@Composable
fun DeleteDataClientFirebase(
    clienteId: String,
    trabajosViewModel: TrabajosViewModel,
    navigationController: NavHostController
) {

    val showDeleteAlertDialog: Boolean by trabajosViewModel.showDeleteDialog.observeAsState(false)

    com.jetPackReformesPujol.Utils.AlertDialog(
        showAlert = showDeleteAlertDialog,
        onDismiss = { trabajosViewModel.dialogDeleteDismiss() },
        onConfirm = { trabajosViewModel.deleteClienteInFirebase(clienteId) },
        titulo = "Desea eliminar el cliente de la base de datos?",
        descripcion = "Eliminará todo el contenido del cliente"
    )

    val clienteEliminado: Boolean by trabajosViewModel.clienteEliminado.collectAsState(false)

    LaunchedEffect(clienteEliminado) {
        if (clienteEliminado) {
            navigationController.popBackStack()
        }
    }
}

@Composable
fun GetDetailsFromFBDatabase(trabajoId: String) {

    val mDatabaseRef =
        FirebaseDatabase.getInstance().getReference("clients").child(trabajoId)

    val clienteId = remember { mutableStateOf<ClienteModelFirebase?>(null) }

    LaunchedEffect(clienteId) {
        mDatabaseRef
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    clienteId.value = document.getValue(ClienteModelFirebase::class.java)
                }
            }
    }
    CardInfoCliente(clienteId)
}

@Composable
fun CardInfoCliente(clienteId: MutableState<ClienteModelFirebase?>) {
    Card(
        Modifier
            .fillMaxWidth(), elevation = 8.dp
    ) {
        Box(
            Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Cyan, Blue
                        )
                    )
                )
        ) {
            clienteId.value?.let { cliente ->
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier.align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = cliente.clienteid!!,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(text = "Fecha: ${cliente.data}", fontSize = 15.sp)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(text = "Nombre: ${cliente.nom}", fontSize = 15.sp)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(text = "Teléfono: ${cliente.telefon}", fontSize = 15.sp)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(text = "Precio: ${cliente.preutotal} €", fontSize = 15.sp)
                    Spacer(modifier = Modifier.size(5.dp))
                }
            }
        }
    }
}

@Composable
fun GetPicturesFromDBStorage(
    isLoading: Boolean,
    selectedPhotoId: (String) -> Unit,
    trabajoId: String,
    trabajosViewModel: TrabajosViewModel
) {

    val urlsPictures by trabajosViewModel.urlsPictures.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        trabajosViewModel.getPicturesFromFirebaseStorageViewModel(trabajoId)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.size(50.dp))
            }
        } else {
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
                items(urlsPictures) { photoId ->
                    Image(
                        painter = rememberAsyncImagePainter(photoId),
                        contentDescription = "Trabajo Detail Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                if (photoId.isNotEmpty()) {
                                    //Antes de pasar una URL como argumento, debe codificarla, así: IMPORTANTE para pasar url de Firebase Storage
                                    selectedPhotoId(
                                        URLEncoder.encode(
                                            photoId,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    )
                                } else {
                                    Timber.d("photoId es nulo o vacío")
                                }
                            }
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}



