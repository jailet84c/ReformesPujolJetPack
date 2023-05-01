package com.jetPackReformesPujol.materiales.domain

import com.jetPackReformesPujol.materiales.data.MaterialesRepository
import com.jetPackReformesPujol.materiales.model.MaterialesModel
import dagger.Provides
import javax.inject.Inject
import javax.inject.Named


class AddMaterialUseCase @Inject constructor(private val materialRepository: MaterialesRepository) {

    suspend operator fun invoke(materialesModel: MaterialesModel) {
        materialRepository.add(materialesModel)
    }
}