package com.jetPackReformesPujol.trabajos.domain

import com.jetPackReformesPujol.trabajos.data.TrabajosRepository
import com.jetPackReformesPujol.trabajos.model.ClienteModel
import javax.inject.Inject

class GetClienteIdUseCase @Inject constructor(private val trabajosRepository: TrabajosRepository) {

    operator fun invoke(idCliente: Int): ClienteModel {
        val trabajosEntity = trabajosRepository.getClienteId(idCliente)
        return ClienteModel(
            trabajosEntity.id,
            trabajosEntity.campingParcela,
            trabajosEntity.fecha,
            trabajosEntity.nombreCliente,
            trabajosEntity.telefono,
            trabajosEntity.precio,
            trabajosEntity.pagaisenal,
            trabajosEntity.imagenesCliente
        )
    }
}
