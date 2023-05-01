package com.jetPackReformesPujol.presupuestos.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.jetPackReformesPujol.R
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EmptyPresupuestoMain(
    presupuestosViewModel: PresupuestosViewModel,
    navHostController: NavHostController,
    navigateUp: () -> Unit
) {

    val datosPresupuesto by presupuestosViewModel.datosPresupuesto.observeAsState()
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
                EmptyDataClient(presupuestosViewModel, datosPresupuesto)
                Box(
                    Modifier
                        .weight(1F)
                        .fillMaxHeight()
                ) {
                    EmptyConcept(presupuestosViewModel, datosPresupuesto)
                }
                Box(Modifier.wrapContentWidth()) {
                    EmptyPrice(presupuestosViewModel, datosPresupuesto)
                }
                EmptyAlertAddPresupuesto(presupuestosViewModel, navHostController)
            }
        }
    }
}

@Composable
fun EmptyDataClient(
    presupuestosViewModel: PresupuestosViewModel,
    datosPresupuesto: PresupuestosModel?
) {
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
fun EmptyConcept(
    presupuestosViewModel: PresupuestosViewModel,
    datosPresupuesto: PresupuestosModel?
) {
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
                value = datosPresupuesto?.precioConcepto?.toString() ?: "",
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
fun EmptyPrice(presupuestosViewModel: PresupuestosViewModel, datosPresupuesto: PresupuestosModel?) {

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
                        value = datosPresupuesto?.precioBase?.toString() ?: "",
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
                        textAlign = TextAlign.End
                    )
                    Spacer(Modifier.size(10.dp))
                    // Precio IVA
                    Text(text = datosPresupuesto?.iva?.toString() ?: "")
                }
                Row(Modifier.fillMaxWidth()) {
                    Text(text = "Precio total :", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.size(10.dp))
                    // Precio Total
                    Text(text = datosPresupuesto?.precio?.toString() ?: "")
                }
            }
        }
    }
}

@Composable
fun EmptyAlertAddPresupuesto(
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


