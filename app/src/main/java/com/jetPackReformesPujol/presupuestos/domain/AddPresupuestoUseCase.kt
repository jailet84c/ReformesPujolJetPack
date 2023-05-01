package com.jetPackReformesPujol.presupuestos.domain

import com.jetPackReformesPujol.presupuestos.data.PresupuestosRepository
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel
import javax.inject.Inject

class AddPresupuestosUseCase @Inject constructor(private val presupuestosRepository: PresupuestosRepository) {

    suspend operator fun invoke(presupuestosModel: PresupuestosModel) {
        presupuestosRepository.add(presupuestosModel)
    }
}