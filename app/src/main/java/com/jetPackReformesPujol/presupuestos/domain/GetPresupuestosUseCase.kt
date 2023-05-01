package com.jetPackReformesPujol.presupuestos.domain

import com.jetPackReformesPujol.presupuestos.data.PresupuestosRepository
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPresupuestosUseCase @Inject constructor(private val presupuestosRepository: PresupuestosRepository) {

    operator fun invoke(): Flow<List<PresupuestosModel>> {
        return presupuestosRepository.presupuestos
    }
}