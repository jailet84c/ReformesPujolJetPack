package com.jetPackReformesPujol.trabajos.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.jetPackReformesPujol.R
import com.jetPackReformesPujol.Utils.MyTopAppBar
import com.jetPackReformesPujol.trabajos.model.ClienteModel
import com.jetPackReformesPujol.trabajos.model.ClienteModelFirebase

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContentTrabajos(
    selectedTrabajo: (String) -> Unit,
    navigationController: NavHostController,
    trabajosViewModel: TrabajosViewModel, navigateUp: () -> Unit
) {

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiStateT by produceState<TrabajosUistate>(
        initialValue = TrabajosUistate.LoadingT,
        key1 = lifecycle,
        key2 = trabajosViewModel

    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            trabajosViewModel.uiStateT.collect { value = it }
        }
    }

    when (uiStateT) {
        is TrabajosUistate.ErrorT -> {
            showError = true
            errorMessage = "Error"

            if (showError) {
                Snackbar(
                    action = {
                        // Acción que se puede realizar
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(errorMessage)
                }
            }
        }
        TrabajosUistate.LoadingT -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(Modifier.size(50.dp))
            }
        }
        is TrabajosUistate.SuccesT -> {

            val showAddTrabajoDialog: Boolean by trabajosViewModel.showDialogAddtrabajo.observeAsState(
                false
            )

            Scaffold(topBar = { MyTopAppBar(navigateUp) }) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    GetDataFromFBDatabase(trabajosViewModel, navigationController, selectedTrabajo)
                    AddTrabajoDialog(trabajosViewModel, showAddTrabajoDialog)
                    FabInsertar(Modifier.align(Alignment.BottomEnd), trabajosViewModel)
                }
            }
        }
    }
}


@Composable
fun GetDataFromFBDatabase(
    trabajosViewModel: TrabajosViewModel,
    navigationController: NavHostController,
    selectedTrabajo: (String) -> Unit
) {

    val clientesList by trabajosViewModel.clientesList.observeAsState(emptyList())

    LazyColumn {
        items(clientesList) { cliente ->
            ItemCliente(
                cliente,
                selectedTrabajo,
                trabajosViewModel
            )
        }
    }
}

@Composable
fun ItemCliente(
    cliente: ClienteModelFirebase,
    selectedTrabajo: (String) -> Unit,
    trabajosViewModel: TrabajosViewModel
) {

    Card(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = cliente.clienteid!!,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(text = "Fecha: " + cliente.data!!, fontSize = 15.sp)
                Spacer(modifier = Modifier.size(5.dp))
                Text(text = "Nombre: " + cliente.nom!!, fontSize = 15.sp)

                Spacer(modifier = Modifier.size(5.dp))
                Text(text = "Teléfono: " + cliente.telefon.toString(), fontSize = 15.sp)
                Spacer(modifier = Modifier.size(5.dp))
                Text(text = "Precio: " + cliente.preutotal.toString() + "€", fontSize = 15.sp)
                Spacer(modifier = Modifier.size(5.dp))
            }
            Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                Image(
                    painter = painterResource(id = R.drawable.reformespujol),
                    contentDescription = "imagenTrabajo",
                    modifier = Modifier
                        .clickable { selectedTrabajo(cliente.clienteid!!) }
                        .size(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .padding(top = 30.dp)
                )
            }
        }
    }
}

@Composable
fun AddTrabajoDialog(trabajosViewModel: TrabajosViewModel, show: Boolean) {

    val clienteFirebase by trabajosViewModel.clienteFirebase.observeAsState()

    var clienteIdValue by remember { mutableStateOf(clienteFirebase?.clienteid ?: "Camping") }
    var fechaIdValue by remember { mutableStateOf(clienteFirebase?.data ?: "Fecha") }
    var nombreIdValue by remember { mutableStateOf(clienteFirebase?.nom ?: "Nombre") }
    var telefonoIdValue by remember { mutableStateOf(clienteFirebase?.telefon ?: "Telefono") }
    var precioIdValue by remember { mutableStateOf(clienteFirebase?.preutotal ?: "Precio") }

    val mMaxLength = 6
    val mMaxLengthTelefono = 11
    val mMaxLengthFecha = 10

    if (show) {
        Dialog(onDismissRequest = { trabajosViewModel.onDialogAddClose() }) {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Añade cliente",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = clienteIdValue,
                    onValueChange = { clienteId ->
                        trabajosViewModel.onTextFieldChanged(
                            clienteId,
                            clienteFirebase?.data,
                            clienteFirebase?.nom,
                            clienteFirebase?.telefon,
                            clienteFirebase?.preutotal,
                        )
                        clienteIdValue = clienteId
                    },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text("Camping") },
                    placeholder = { Text("Camping") },
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = fechaIdValue,
                    onValueChange = { fecha ->
                        if (fecha.length <= mMaxLengthFecha)
                            trabajosViewModel.onTextFieldChanged(
                                clienteFirebase?.clienteid,
                                fecha,
                                clienteFirebase?.nom,
                                clienteFirebase?.telefon ?: "",
                                clienteFirebase?.preutotal ?: "",
                            )
                        fechaIdValue = fecha
                    },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text("Fecha") },
                    placeholder = { Text("Fecha") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = nombreIdValue,
                    onValueChange = { nombre ->
                        trabajosViewModel.onTextFieldChanged(
                            clienteFirebase?.clienteid,
                            clienteFirebase?.data,
                            nombre,
                            clienteFirebase?.telefon,
                            clienteFirebase?.preutotal
                        )
                        nombreIdValue = nombre
                    },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text("Nombre") },
                    placeholder = { Text("Nombre") },
                    keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
                    )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = telefonoIdValue,
                    onValueChange = { telefono ->
                        if (telefono.length <= mMaxLengthTelefono && telefono.isNotEmpty())
                            trabajosViewModel.onTextFieldChanged(
                                clienteFirebase?.clienteid,
                                clienteFirebase?.data,
                                clienteFirebase?.nom,
                                telefono,
                                clienteFirebase?.preutotal
                            )
                        telefonoIdValue = telefono
                    },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text("Teléfono") },
                    placeholder = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = precioIdValue,
                    onValueChange = { precio ->
                        if (precio.length <= mMaxLength && precio.isNotEmpty())
                            trabajosViewModel.onTextFieldChanged(
                                clienteFirebase?.clienteid,
                                clienteFirebase?.data,
                                clienteFirebase?.nom,
                                clienteFirebase?.telefon,
                                precio
                            )
                        precioIdValue = precio
                    },
                    singleLine = true,
                    maxLines = 1,
                    label = { Text("Precio") },
                    placeholder = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        trabajosViewModel.addClienteToFirebase(clienteFirebase!!)

                        clienteIdValue = ""
                        fechaIdValue = ""
                        nombreIdValue = ""
                        telefonoIdValue = ""
                        precioIdValue = ""
                    }
                ) {
                    Text(text = "Añadir cliente")
                }
            }
        }
    }
}


@Composable
fun FabInsertar(modifier: Modifier, trabajosViewModel: TrabajosViewModel) {
    FloatingActionButton(
        onClick = { trabajosViewModel.onShowDialogAddTrabajo() },
        modifier = modifier.padding(16.dp)
    ) {
        Icon(
            Icons.Filled.Add, contentDescription = "",
            modifier = Modifier.padding(16.dp)
        )
    }
}
