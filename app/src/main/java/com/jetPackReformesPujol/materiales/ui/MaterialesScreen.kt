package com.jetPackReformesPujol.materiales.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.jetPackReformesPujol.Utils.MyTopAppBar
import com.jetPackReformesPujol.materiales.model.MaterialesModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContentMateriales(
    navigationController: NavHostController,
    materialesViewModel: MaterialesViewModel, navigateUp: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<MaterialesUistate>(
        initialValue = MaterialesUistate.Loading,
        key1 = lifecycle,
        key2 = materialesViewModel

    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            materialesViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is MaterialesUistate.Error -> {}
        MaterialesUistate.Loading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(Modifier.size(50.dp))
            }
        }
        is MaterialesUistate.Succes -> {
            Scaffold(topBar = { MyTopAppBar(navigateUp) }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    MaterialesList(
                        (uiState as MaterialesUistate.Succes).materiales,
                        materialesViewModel
                    )
                    AddDialog(materialesViewModel)
                    FabDialog(Modifier.align(Alignment.BottomEnd), materialesViewModel)
                }
            }
        }
    }
}

@Composable
fun MaterialesList(
    materiales: List<MaterialesModel>,
    materialesViewModel: MaterialesViewModel,
) {

    val selectedMaterialId: Int? by materialesViewModel.selectedMaterialId.observeAsState(null)

    LazyColumn {
        items(materiales, key = { it.id }) { material ->
            ItemMaterial(
                material,
                materialesViewModel,
                isSelected = (material.id == selectedMaterialId)
            )
        }
    }
}

@Composable
fun ItemMaterial(
    modeloMaterial: MaterialesModel,
    materialesViewModel: MaterialesViewModel,
    isSelected: Boolean
) {

    val showDialogUpdate: Boolean by materialesViewModel.showDialogUpdate.collectAsState(false)
    val showAlertDelete: Boolean by materialesViewModel.showAlertDelete.observeAsState(false)

    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    materialesViewModel.onOpenAlertDeleteClicked(modeloMaterial.id)
                })
            },
        elevation = 8.dp
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = modeloMaterial.material, modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .size(25.dp)
            )
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(
                        enabled = true,
                        onClick = {
                            materialesViewModel.setSelectedMaterialId(modeloMaterial.id)
                        }),
                imageVector = Icons.Default.Edit,
                contentDescription = ""
            )

            CustomDialog(
                show = isSelected && showDialogUpdate,
                value = modeloMaterial.material,
                titulo = "Actualizar material",
                textButton = "Actualizar material",
                onDismiss = { materialesViewModel.resetSelectedMaterialId() },
                onConfirm = { nuevoValor ->
                    materialesViewModel.onMaterialUpdate(modeloMaterial, nuevoValor)
                }
            )

            com.jetPackReformesPujol.Utils.AlertDialog(
                isSelected && showAlertDelete,
                onDismiss = { materialesViewModel.onAlertDismiss() },
                onConfirm = { materialesViewModel.onItemDelete(modeloMaterial) },
                "Eliminar item",
                "Desea eliminar este item?"
            )
        }
    }
}

@Composable
fun AddDialog(materialesViewModel: MaterialesViewModel) {
    val showDialog: Boolean by materialesViewModel.showDialog.observeAsState(false)
    val txtField = remember { mutableStateOf("") }
    CustomDialog(
        show = showDialog,
        value = txtField.value,
        titulo = "Añadir material",
        textButton = "Añadir material",
        onDismiss = { materialesViewModel.onDialogClose() },
        onConfirm = { nuevoMaterial ->
            materialesViewModel.onMaterialCreated(nuevoMaterial)
        }
    )
}

@Composable
fun FabDialog(modifier: Modifier, materialesViewModel: MaterialesViewModel) {
    FloatingActionButton(
        onClick = { materialesViewModel.onShowDialogClick() },
        modifier = modifier.padding(16.dp)
    ) {
        Icon(
            Icons.Filled.Add, contentDescription = "",
            modifier = Modifier.padding(16.dp)
        )
    }
}



