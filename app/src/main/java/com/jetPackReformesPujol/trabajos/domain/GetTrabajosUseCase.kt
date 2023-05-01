package com.jetPackReformesPujol.trabajos.domain

import com.jetPackReformesPujol.trabajos.data.TrabajosRepository
import com.jetPackReformesPujol.trabajos.model.ClienteModel
import com.jetPackReformesPujol.trabajos.model.ClienteModelFirebase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrabajosUseCase @Inject constructor(private val trabajosRepository: TrabajosRepository) {

    operator fun invoke(): Flow<List<ClienteModel>> {
        return trabajosRepository.clientes
    }
}