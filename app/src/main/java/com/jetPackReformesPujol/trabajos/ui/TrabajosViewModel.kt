package com.jetPackReformesPujol.trabajos.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jetPackReformesPujol.trabajos.domain.FirebaseRepository
import com.jetPackReformesPujol.trabajos.domain.*
import com.jetPackReformesPujol.trabajos.model.ClienteModel
import com.jetPackReformesPujol.trabajos.model.ClienteModelFirebase
import com.jetPackReformesPujol.trabajos.ui.TrabajosUistate.SuccesT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class TrabajosViewModel @Inject constructor(
    private val addTrabajosUseCase: AddTrabajosUseCase,
    private val updateTrabajosUseCase: UpdateTrabajosUseCase,
    private val deleteTrabajosUseCase: DeleteTrabajosUseCase,
    getTrabajosUseCase: GetTrabajosUseCase,
    private val getClienteIdUseCase: GetClienteIdUseCase,
    private val deleteClienteFirebase: FirebaseRepository,
    private val addClienteFirebase: FirebaseRepository,
    private val deletePicturesFirebaseStorage: FirebaseRepository,
    private val getPicturesFromFirebaseStorage: FirebaseRepository,
    private val addPicturesFromStorageToFireBaseStorage: FirebaseRepository
) : ViewModel() {

    init {
        getDataFromFireBaseDataBase()
    }

    val uiStateT: StateFlow<TrabajosUistate> = getTrabajosUseCase().map(::SuccesT)
        .catch { TrabajosUistate.ErrorT(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TrabajosUistate.LoadingT)

    private val _showDialogAddTrabajp = MutableLiveData<Boolean>()
    val showDialogAddtrabajo: LiveData<Boolean> = _showDialogAddTrabajp

    private val _showDeleteDialog = MutableLiveData<Boolean>()
    val showDeleteDialog: LiveData<Boolean> = _showDeleteDialog


    private val _clienteEliminado = MutableStateFlow(false)
    val clienteEliminado: StateFlow<Boolean>
        get() = _clienteEliminado

    private val _imagenEliminada = MutableStateFlow(false)
    val imagenEliminada: StateFlow<Boolean>
        get() = _imagenEliminada

    /*private val _clienteData = MutableLiveData<ClienteModel>()
    val clienteData: LiveData<ClienteModel> = _clienteData*/

    private val _clienteFirebase =
        MutableLiveData<ClienteModelFirebase>(ClienteModelFirebase("", "", "", "", ""))
    val clienteFirebase: LiveData<ClienteModelFirebase> = _clienteFirebase

    private val _clientesList = MutableLiveData<List<ClienteModelFirebase>>()
    val clientesList: LiveData<List<ClienteModelFirebase>> = _clientesList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _urlsPictures = MutableStateFlow<List<String>>(emptyList())
    val urlsPictures: StateFlow<List<String>> = _urlsPictures

    private val _selectedImageUri = MutableLiveData<Uri>(null)
    val selectedImageUri: LiveData<Uri> = _selectedImageUri

    fun getDataFromFireBaseDataBase() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("clients")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<ClienteModelFirebase>()
                for (child in snapshot.children) {
                    val value = child.getValue(ClienteModelFirebase::class.java)
                    if (value != null) {
                        dataList.add(value)
                    }
                }
                _clientesList.postValue(dataList)
                Timber.tag("FireBase").d("FireBase OK")
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.tag("FireBase").e(error.toException(), "FireBase onCancelled")
            }
        })
    }

    fun getPicturesFromFirebaseStorageViewModel(trabajoId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val listUrls =
                    getPicturesFromFirebaseStorage.getPicturesFromFirebaseStorage(trabajoId)
                _urlsPictures.value = listUrls
                _isLoading.value = false
            } catch (e: Exception) {
                Timber.tag("GetPicturesFromDB").e(e, "Error: %s", e.message)
            }
        }
    }

    fun addClienteToFirebase(clienteId: ClienteModelFirebase) {
        addClienteFirebase.addClienteFirebase(clienteId)
        _showDialogAddTrabajp.value = false
    }

    fun addPicturesFromStorageToFBStorage(clienteId: String) {
        viewModelScope.launch {
                addPicturesFromStorageToFireBaseStorage.addPicturesFromStorageToFireBaseStorage(
                    clienteId,
                    _selectedImageUri.value
                )
            delay(6000)
            val listNewUrls =
                getPicturesFromFirebaseStorage.getPicturesFromFirebaseStorage(clienteId)
            _urlsPictures.value = listNewUrls
        }
    }

    fun updateSelectedImageUri(uri: Uri) {
        _selectedImageUri.value = uri
    }

    fun deleteClienteInFirebase(cliente: String) {
        _showDeleteDialog.value = true
        deleteClienteFirebase.deleteClienteFirebase(cliente)
        _showDeleteDialog.value = false
        _clienteEliminado.value = true
    }

    fun deletePictureToFBStorage(photoId: String) {
        _showDeleteDialog.value = true
        deletePicturesFirebaseStorage.deletePicturesFirebaseStorage(photoId)
        _showDeleteDialog.value = false
        _imagenEliminada.value = true
    }

    fun onShowDialogAddTrabajo() {
        _showDialogAddTrabajp.value = true
    }

    fun onDialogAddClose() {
        _showDialogAddTrabajp.value = false
    }

    fun onShowDeleteAlertDialog() {
        _showDeleteDialog.value = true
    }

    fun dialogDeleteDismiss() {
        _showDeleteDialog.value = false
    }

    fun onTextFieldChanged(
        clienteId: String?,
        data: String?,
        nom: String?,
        telefon: String?,
        preutotal: String?,
    ) {

        _clienteFirebase.value = _clienteFirebase.value?.let {
            ClienteModelFirebase(
                clienteId,
                data,
                nom,
                telefon,
                preutotal
            )
        }
    }

    /*fun addClienteInRoom(cliente: ClienteModel) {
        _showDialogAddTrabajp.value = false
        viewModelScope.launch {
            addTrabajosUseCase(cliente)
        }
    }*/

    fun updateTrabajoInRoom(clienteModel: ClienteModel) {
        /*_showDialogUpdate.value = false
        viewModelScope.launch {
            updateMaterialesUseCase(materialesModel.copy(material = materialesModel.material))
        }*/
    }

    /*fun onTextFieldChanged(
        campingParcela: String?,
        fecha: String?,
        nombre: String?,
        telefono: Long?,
        precio: Int?,
        pagaisenal: Int?
    ) {

        _clienteData.value = _clienteData.value?.let { nuevoCliente ->
            ClienteModel(
                id = nuevoCliente.id,
                campingParcela = campingParcela!!,
                fecha = fecha,
                nombreCliente = nombre,
                telefono = telefono,
                precio = precio,
                pagaisenal = pagaisenal
            )
        }
    }*/

    suspend fun getClienteId(idCliente: Int): ClienteModel {
        val clienteEntity = withContext(Dispatchers.IO) {
            getClienteIdUseCase(idCliente)
        }
        val cliente = ClienteModel(
            clienteEntity.id,
            clienteEntity.campingParcela,
            clienteEntity.fecha,
            clienteEntity.nombreCliente,
            clienteEntity.telefono,
            clienteEntity.precio,
            clienteEntity.pagaisenal,
            clienteEntity.imagenesCliente
        )
        return cliente
    }
}


