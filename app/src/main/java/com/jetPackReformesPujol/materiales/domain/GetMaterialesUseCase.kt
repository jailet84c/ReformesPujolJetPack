package com.jetPackReformesPujol.materiales.domain

import com.jetPackReformesPujol.materiales.data.MaterialesRepository
import com.jetPackReformesPujol.materiales.model.MaterialesModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMaterialesUseCase @Inject constructor(private val materialesRepository: MaterialesRepository) {

    operator fun invoke(): Flow<List<MaterialesModel>> {
        return materialesRepository.materiales
    }
}