package com.jetPackReformesPujol.materiales.data

import com.jetPackReformesPujol.data.MaterialesEntity
import com.jetPackReformesPujol.materiales.model.MaterialesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MaterialesRepository @Inject constructor(private val materialesDao: MaterialesDao) {

    val materiales: Flow<List<MaterialesModel>> = materialesDao.getMateriales().map { items -> items.map { MaterialesModel(it.id, it.material) } }

    suspend fun add(materialesModel: MaterialesModel){
        materialesDao.addMaterial(materialesModel.toData())
    }

    suspend fun update(materialesModel: MaterialesModel){
        materialesDao.updateMaterial(materialesModel.toData())
    }

    suspend fun delete(materialesModel: MaterialesModel){
        materialesDao.deleteMaterial(materialesModel.toData())
    }
}

fun MaterialesModel.toData(): MaterialesEntity {
    return MaterialesEntity(this.id, this.material)
}
