package com.jetPackReformesPujol.trabajos.domain

import com.jetPackReformesPujol.trabajos.data.TrabajosRepository
import com.jetPackReformesPujol.trabajos.model.ClienteModel
import javax.inject.Inject

class AddTrabajosUseCase @Inject constructor(private val trabajosRepository: TrabajosRepository) {

    suspend operator fun invoke(clienteModel: ClienteModel) {
        trabajosRepository.add(clienteModel)
    }
}