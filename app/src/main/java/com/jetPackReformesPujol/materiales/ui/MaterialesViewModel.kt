package com.jetPackReformesPujol.materiales.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetPackReformesPujol.materiales.domain.AddMaterialUseCase
import com.jetPackReformesPujol.materiales.domain.DeleteMaterialUseCase
import com.jetPackReformesPujol.materiales.domain.GetMaterialesUseCase
import com.jetPackReformesPujol.materiales.domain.UpdateMaterialUsecase
import com.jetPackReformesPujol.materiales.model.MaterialesModel
import com.jetPackReformesPujol.materiales.ui.MaterialesUistate.Succes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialesViewModel @Inject constructor(
                                              private val addMaterialesUseCase: AddMaterialUseCase,
                                              private val updateMaterialesUseCase: UpdateMaterialUsecase,
                                              private val deleteMaterialesUseCase: DeleteMaterialUseCase,
                                              getMaterialesUseCase: GetMaterialesUseCase
):ViewModel() {

    val uiState: StateFlow<MaterialesUistate> = getMaterialesUseCase().map(::Succes)
        .catch { MaterialesUistate.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MaterialesUistate.Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _showAlertDelete = MutableLiveData<Boolean>()
    val showAlertDelete: LiveData<Boolean> = _showAlertDelete

    /*private val _showDialogUpdate = MutableLiveData<Boolean>()
    val showDialogUpdate: LiveData<Boolean> = _showDialogUpdate*/

    private val _showDialogUpdate = MutableStateFlow(false)
    val showDialogUpdate: StateFlow<Boolean> = _showDialogUpdate

    private val _selectedMaterialId = MutableLiveData<Int?>(null)
    val selectedMaterialId: LiveData<Int?> = _selectedMaterialId

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onMaterialCreated(material: String) {
        _showDialog.value = false
        viewModelScope.launch {
            val newMaterial = MaterialesModel(material = material)
            addMaterialesUseCase(newMaterial)
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun setSelectedMaterialId(id: Int) {
        _showDialogUpdate.value = true
        _selectedMaterialId.value = id
    }

    fun resetSelectedMaterialId() {
        _showDialogUpdate.value = false
        _selectedMaterialId.value = null
    }

    fun onMaterialUpdate(materialesModel: MaterialesModel,materialRenovado : String) {
        _showDialogUpdate.value = false
        viewModelScope.launch {
            val updatedMaterial = materialesModel.copy(material = materialRenovado)
            updateMaterialesUseCase(updatedMaterial)
        }
    }

    fun onOpenAlertDeleteClicked(id: Int) {
        _showAlertDelete.value = true
        _selectedMaterialId.value = id
    }

    fun onAlertDismiss() {
        _showAlertDelete.value = false
    }

    fun onItemDelete(material: MaterialesModel) {
        viewModelScope.launch {
            deleteMaterialesUseCase(material)
        }
        _showAlertDelete.value = false
    }
}

