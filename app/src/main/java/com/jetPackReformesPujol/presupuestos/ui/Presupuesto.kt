package com.jetPackReformesPujol.presupuestos.ui

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.jetPackReformesPujol.R
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PresupuestoMain(
    presupuestoId: Int?,
    presupuestosViewModel: PresupuestosViewModel,
    navHostController: NavHostController,
    navigateUp: () -> Unit
) {

    val datosPresupuesto by presupuestosViewModel.datosPresupuesto.observeAsState()

    if (presupuestoId != null) {
        LaunchedEffect(presupuestoId) {
            presupuestosViewModel.getOnePresupuestoById(presupuestoId) ?: PresupuestosModel()
        }
    }

    // Establecer el valor predeterminado en -1 si presupuestoId es nulo
    val id = presupuestoId ?: -1

    val padding = 16.dp

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
                if (presupuestoId != null) {
                    IconButton(onClick = {
                        presupuestosViewModel.onShowDeleteAlertDialog()
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete presupuesto")
                    }
                }
                IconButton(onClick = {
                    presupuestosViewModel.onShowAddAlertDialog()
                }) {
                    Icon(Icons.Filled.Save, contentDescription = "Save presupuesto")
                }
            })
    }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column() {
                DataClient(presupuestosViewModel, datosPresupuesto)
                Box(
                    Modifier
                        .weight(1F)
                        .fillMaxHeight()
                ) {
                    Concept(presupuestosViewModel, datosPresupuesto)
                }
                Box(Modifier.wrapContentWidth()) {
                    Price(presupuestosViewModel, datosPresupuesto)
                }
                AlertAddPresupuesto(presupuestosViewModel, navHostController)
                if (presupuestoId != null) {
                    AlertDeletePresupuesto(presupuestosViewModel, id, navHostController)
                }
            }
        }
    }
}

@Composable
fun DataClient(presupuestosViewModel: PresupuestosViewModel, datosPresupuesto: PresupuestosModel?) {

    Row(
        Modifier
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = R.drawable.reformespujol),
            contentDescription = "logo",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.size(20.dp))

        Card(
            modifier = Modifier
                .width(250.dp)
                .height(150.dp)
                .border(BorderStroke(1.dp, Color(0xFF416E8A))),
            backgroundColor = (Color(0xFF8BD2FF)),
            elevation = 8.dp,
            border = BorderStroke(1.dp, Color.LightGray)
        ) {

            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Fecha :",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    BasicTextField(
                        value = datosPresupuesto?.fecha ?: "",
                        onValueChange = { newFecha ->
                            val updatedPresupuesto =
                                datosPresupuesto?.copy(fecha = newFecha) ?: PresupuestosModel()
                            presupuestosViewModel.onPresupuestoChanged(updatedPresupuesto)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Dirección :",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    BasicTextField(
                        value = datosPresupuesto?.direccion ?: "",
                        onValueChange = { newDireccion ->
                            val updatedPresupuesto =
                                datosPresupuesto?.copy(direccion = newDireccion)
                                    ?: PresupuestosModel()
                            presupuestosViewModel.onPresupuestoChanged(updatedPresupuesto)
                        },
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nombre :",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    BasicTextField(
                        value = datosPresupuesto?.nombre ?: "",
                        onValueChange = { newNombre ->
                            val updatedPresupuesto = datosPresupuesto?.copy(nombre = newNombre)
                                ?: PresupuestosModel()
                            presupuestosViewModel.onPresupuestoChanged(updatedPresupuesto)
                        },
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Teléfono :",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    BasicTextField(
                        value = datosPresupuesto?.telefono.toString().takeIf { it != "null" }
                            ?.trimStart('0') ?: "",
                        onValueChange = { newTelefono ->
                            newTelefono.takeIf { it.isNotEmpty() && it.isDigitsOnly() }
                                ?.let {
                                    val updatedPresupuesto =
                                        datosPresupuesto?.copy(telefono = newTelefono.toLong())
                                            ?: PresupuestosModel()
                                    presupuestosViewModel.onPresupuestoChanged(
                                        updatedPresupuesto
                                    )
                                }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }
        }
    }
    Spacer(Modifier.size(20.dp))
}

@Composable
fun Concept(presupuestosViewModel: PresupuestosViewModel, datosPresupuesto: PresupuestosModel?) {
    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color(0xFF8BD2FF)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Descripción",
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(end = 40.dp),
                text = "Precio",
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .weight(0.7f),
                value = datosPresupuesto?.concepto ?: "",
                placeholder = { Text("Concepto") },
                onValueChange = { newConcepto ->
                    val updatedPresupuesto =
                        datosPresupuesto?.copy(concepto = newConcepto) ?: PresupuestosModel()
                    presupuestosViewModel.onPresupuestoChanged(updatedPresupuesto)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                modifier = Modifier
                    .weight(0.3f),
                value = datosPresupuesto?.precioConcepto.toString().takeIf { it != "null" }
                    ?.trimStart('0') ?: "",
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Precio") },
                onValueChange = { newPrecioConcepto ->
                    newPrecioConcepto.takeIf { it.isNotEmpty() && it.matches(Regex("^\\d+(\\.\\d{0,2})?$")) }
                        ?.let {
                            val updatedPresupuesto =
                                datosPresupuesto?.copy(precioConcepto = newPrecioConcepto.toFloat())
                                    ?: PresupuestosModel()
                            presupuestosViewModel.onPresupuestoChanged(updatedPresupuesto)
                        }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
    Spacer(modifier = Modifier.size(5.dp))
}

@Composable
fun Price(presupuestosViewModel: PresupuestosViewModel, datosPresupuesto: PresupuestosModel?) {

    val precioBase = remember { mutableStateOf(datosPresupuesto?.precioBase ?: 0.00F) }
    val iva = remember { mutableStateOf(datosPresupuesto?.iva ?: 0.00F) }
    val precioTotal = remember { mutableStateOf(datosPresupuesto?.precio ?: 0.00f) }

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Card(
            Modifier
                .width(200.dp)
                .height(100.dp)
                .border(BorderStroke(1.dp, Color(0xFF416E8A))),
            backgroundColor = (Color(0xFF8BD2FF)),
            elevation = 8.dp,
            border = BorderStroke(1.dp, Color.LightGray),
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Text(text = "Precio base :", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.size(10.dp))
                    // Precio Base
                    BasicTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = datosPresupuesto?.precioBase.toString(),
                        onValueChange = { newPrecioBase ->
                            val decimalIndex = newPrecioBase.indexOf('.')
                            if (decimalIndex != -1) {
                                // Si se ha ingresado un punto, solo se permite editar la parte decimal
                                if (newPrecioBase.substring(decimalIndex + 1).length <= 2) {

                                    precioBase.value = newPrecioBase.toFloat()
                                    iva.value = newPrecioBase.toFloat() * 0.21F
                                    precioTotal.value = precioBase.value * 1.21F

                                    val updatedPresupuesto =
                                        datosPresupuesto?.copy(
                                            precioBase = precioBase.value,
                                            iva = iva.value,
                                            precio = precioTotal.value
                                        )
                                            ?: PresupuestosModel(
                                                precioBase = precioBase.value,
                                                iva = iva.value,
                                                precio = precioTotal.value
                                            )
                                    presupuestosViewModel.onPresupuestoChanged(updatedPresupuesto)
                                }
                            } else {
                                // Si no se ha ingresado un punto, se permite editar toda la cadena
                                precioBase.value = newPrecioBase.toFloat()
                                iva.value = newPrecioBase.toFloat() * 0.21F
                                precioTotal.value = precioBase.value * 1.21F
                                val updatedPresupuesto =
                                    datosPresupuesto?.copy(
                                        precioBase = precioBase.value,
                                        iva = iva.value,
                                        precio = precioTotal.value
                                    )
                                        ?: PresupuestosModel(
                                            precioBase = precioBase.value,
                                            iva = iva.value,
                                            precio = precioTotal.value
                                        )
                                presupuestosViewModel.onPresupuestoChanged(updatedPresupuesto)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "IVA (21%) :   ",
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.size(10.dp))
                    // Precio Total
                    Text(text = datosPresupuesto?.precio.toString())
                }
                Row(Modifier.fillMaxWidth()) {
                    Text(text = "Precio total :", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.size(10.dp))
                    // Precio IVA
                    Text(text = datosPresupuesto?.iva.toString())
                }
            }
        }
    }
}

@Composable
fun AlertAddPresupuesto(
    presupuestosViewModel: PresupuestosViewModel,
    navHostController: NavHostController
) {

    val showAlertAdd: Boolean by presupuestosViewModel.showAlertAdd.observeAsState(false)
    val datosPresupuesto by presupuestosViewModel.datosPresupuesto.observeAsState()

    com.jetPackReformesPujol.Utils.AlertDialog(
        showAlert = showAlertAdd,
        onDismiss = { presupuestosViewModel.dialogAddDismiss() },
        onConfirm = {
            presupuestosViewModel.onAddPresupuestoInRoom(
                PresupuestosModel(
                    fecha = datosPresupuesto!!.fecha,
                    direccion = datosPresupuesto!!.direccion,
                    nombre = datosPresupuesto!!.nombre,
                    telefono = datosPresupuesto!!.telefono,
                    concepto = datosPresupuesto!!.concepto,
                    precioConcepto = datosPresupuesto!!.precioConcepto,
                    precioBase = datosPresupuesto!!.precioBase,
                    iva = datosPresupuesto!!.iva,
                    precio = datosPresupuesto!!.precio
                )
            )
            navHostController.popBackStack()
        },
        titulo = "Guardar presupuesto ?",
        descripcion = "Guardará el contenido del presupuesto actual"
    )
}

@Composable
fun AlertDeletePresupuesto(
    presupuestosViewModel: PresupuestosViewModel,
    presupuestoId: Int, navHostController: NavHostController
) {

    val showAlertDelete: Boolean by presupuestosViewModel.showAlertDelete.observeAsState(false)

    com.jetPackReformesPujol.Utils.AlertDialog(
        showAlert = showAlertDelete,
        onDismiss = { presupuestosViewModel.dialogDeleteDismiss() },
        onConfirm = {
            presupuestosViewModel.deletePresupuesto(PresupuestosModel(presupuestoId))
            navHostController.popBackStack()
        },
        titulo = "Desea eliminar el presupuesto actual?",
        descripcion = "Eliminará todo el contenido de presupuesto"
    )
}


