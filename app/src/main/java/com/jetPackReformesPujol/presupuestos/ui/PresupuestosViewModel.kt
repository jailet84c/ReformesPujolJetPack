package com.jetPackReformesPujol.presupuestos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetPackReformesPujol.presupuestos.domain.AddPresupuestosUseCase
import com.jetPackReformesPujol.presupuestos.domain.DeletePresupuestosUseCase
import com.jetPackReformesPujol.presupuestos.domain.GetOnePresupuestoUseCase
import com.jetPackReformesPujol.presupuestos.domain.GetPresupuestosUseCase
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PresupuestosViewModel @Inject constructor(
    private val getPresupuestosUseCase: GetPresupuestosUseCase,
    private val addPresupuestosUseCase: AddPresupuestosUseCase,
    private val deletePresupuestosUseCase: DeletePresupuestosUseCase,
    private val getOnePresupuestoUseCase: GetOnePresupuestoUseCase

) : ViewModel() {

    private val _presupuestos = MutableLiveData<List<PresupuestosModel>>()
    val presupuestos: LiveData<List<PresupuestosModel>> = _presupuestos

    init {
        viewModelScope.launch {
            getPresupuestosUseCase().collect {
                _presupuestos.value = it
            }
        }
    }

    private val _showAlertAdd = MutableLiveData<Boolean>()
    val showAlertAdd: LiveData<Boolean> = _showAlertAdd

    private val _showAlertDelete = MutableLiveData<Boolean>()
    val showAlertDelete: LiveData<Boolean> = _showAlertDelete

    private val _datosPresupuesto = MutableLiveData<PresupuestosModel>()
    val datosPresupuesto: LiveData<PresupuestosModel> = _datosPresupuesto

    suspend fun getOnePresupuestoById(id: Int?): PresupuestosModel? {
        return withContext(Dispatchers.IO) {
            val presuspuestoData = getOnePresupuestoUseCase(id!!)
            _datosPresupuesto.postValue(presuspuestoData)
            presuspuestoData
        }
    }

    fun onPresupuestoChanged(dataPresupuesto: PresupuestosModel) {
        _datosPresupuesto.value = dataPresupuesto.copy()
    }

    fun onShowAddAlertDialog() {
        _showAlertAdd.value = true
    }

    fun onAddPresupuestoInRoom(presupuestosModel: PresupuestosModel) {
        _showAlertAdd.value = false
        viewModelScope.launch {
            addPresupuestosUseCase(presupuestosModel)
        }
    }

    fun dialogAddDismiss() {
        _showAlertAdd.value = false
    }

    fun onShowDeleteAlertDialog() {
        _showAlertDelete.value = true
    }

    fun dialogDeleteDismiss() {
        _showAlertDelete.value = false
    }

    fun deletePresupuesto(presupuestosModel: PresupuestosModel) {
        _showAlertDelete.value = false
        viewModelScope.launch {
            deletePresupuestosUseCase(presupuestosModel)
        }
    }
}