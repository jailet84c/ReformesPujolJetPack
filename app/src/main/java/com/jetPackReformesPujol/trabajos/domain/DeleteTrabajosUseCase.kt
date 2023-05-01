package com.jetPackReformesPujol.trabajos.domain

import com.jetPackReformesPujol.trabajos.data.TrabajosRepository
import com.jetPackReformesPujol.trabajos.model.ClienteModel
import com.jetPackReformesPujol.trabajos.model.ClienteModelFirebase
import javax.inject.Inject

class DeleteTrabajosUseCase @Inject constructor(private val trabajosRepository: TrabajosRepository) {

    suspend operator fun invoke(clienteModel: ClienteModel) {
        trabajosRepository.delete(clienteModel)
    }
}