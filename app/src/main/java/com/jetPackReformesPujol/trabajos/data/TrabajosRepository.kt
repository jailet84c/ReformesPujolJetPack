package com.jetPackReformesPujol.trabajos.data

import com.jetPackReformesPujol.data.ClienteEntity
import com.jetPackReformesPujol.trabajos.model.ClienteModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrabajosRepository @Inject constructor(private val clienteDao: ClienteDao) {

    val clientes: Flow<List<ClienteModel>> =
        clienteDao.getClientes().map { items ->
            items.map {
                ClienteModel(
                    it.id,
                    it.campingParcela,
                    it.fecha,
                    it.nombreCliente,
                    it.telefono,
                    it.precio,
                    it.pagaisenal,
                    it.imagenesCliente
                )
            }
        }

    fun getClienteId(idCliente: Int): ClienteEntity {
        return clienteDao.getClienteById(idCliente)
    }

    suspend fun add(clienteModel: ClienteModel) {
        clienteDao.addCliente(clienteModel.toData())
    }

    suspend fun update(clienteModel: ClienteModel) {
        clienteDao.updateCliente(clienteModel.toData())
    }

    suspend fun delete(clienteModel: ClienteModel) {
        clienteDao.deleteCliente(clienteModel.toData())
    }
}

fun ClienteModel.toData(): ClienteEntity {
    return ClienteEntity(
        this.id,
        this.campingParcela,
        this.fecha ?: "",
        this.nombreCliente ?: "",
        this.telefono ?: 0,
        this.precio ?: 0,
        this.pagaisenal ?: 0,
        this.imagenesCliente ?: ""
    )
}