package com.jetPackReformesPujol.presupuestos.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jetPackReformesPujol.Utils.MyTopAppBar
import com.jetPackReformesPujol.model.Routes
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContentPresupuesto(
    selectedPresupuesto: (Int) -> Unit,
    presupuestosViewModel: PresupuestosViewModel,
    navigationController: NavHostController,
    navigateUp: () -> Unit
) {
    Scaffold(topBar = { MyTopAppBar(navigateUp) }) {
        Box(modifier = Modifier.fillMaxSize()) {
            PresupuestosList(
                presupuestosViewModel.presupuestos.value ?: emptyList(),
                selectedPresupuesto,
                presupuestosViewModel
            )
            FABNuevoPresupuesto(
                Modifier.align(Alignment.BottomEnd),
                navigationController
            )
        }
    }
}

@Composable
fun FABNuevoPresupuesto(
    modifier: Modifier,
    navigationController: NavHostController
) {
    FloatingActionButton(
        onClick = { navigationController.navigate(Routes.EmptyPresupuesto.route) },
        modifier = modifier.padding(16.dp)
    ) {
        Icon(
            Icons.Filled.Add, contentDescription = "",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun PresupuestosList(
    listaPresupuestos: List<PresupuestosModel>,
    selectedPresupuesto: (Int) -> Unit,
    presupuestosViewModel: PresupuestosViewModel
) {
    LazyColumn {
        items(listaPresupuestos, key = { it.id }) { presupuesto ->
            ItemPresupuesto(
                presupuesto,
                selectedPresupuesto,
            )
        }
    }
}

@Composable
fun ItemPresupuesto(
    modeloPresupuesto: PresupuestosModel,
    selectedPresupuesto: (Int) -> Unit,
) {
    val horizontalModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 8.dp)

    val textStyle = TextStyle(fontSize = 16.sp)

    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .clickable { selectedPresupuesto(modeloPresupuesto.id) },
        elevation = 8.dp
    ) {
        Column {
            Row(
                horizontalModifier,
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = modeloPresupuesto.direccion,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = modeloPresupuesto.nombre,
                    style = textStyle
                )
            }
            Row(
                horizontalModifier,
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = modeloPresupuesto.fecha,
                    fontSize = 18.sp
                )
                Text(
                    text = "${modeloPresupuesto.precio} €",
                    fontWeight = FontWeight.Bold,
                    style = textStyle
                )
            }
        }
    }
}

/*@Preview
@Composable
fun ver() {
    ItemPresupuesto(modeloPresupuesto = PresupuestosModel(), selectedPresupuesto = {})
}*/
