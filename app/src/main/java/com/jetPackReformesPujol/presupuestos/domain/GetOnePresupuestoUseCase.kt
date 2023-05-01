package com.jetPackReformesPujol.presupuestos.domain

import com.jetPackReformesPujol.presupuestos.data.PresupuestosRepository
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOnePresupuestoUseCase @Inject constructor(private val presupuestosRepository: PresupuestosRepository) {

    suspend operator fun invoke(id: Int): PresupuestosModel? {
        return presupuestosRepository.getOnePresupuestoById(id)

    }
}