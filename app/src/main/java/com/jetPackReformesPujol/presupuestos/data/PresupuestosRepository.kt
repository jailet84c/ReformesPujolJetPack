package com.jetPackReformesPujol.presupuestos.data

import com.jetPackReformesPujol.data.PresupuestoEntity
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresupuestosRepository @Inject constructor(private val presupuestosDao: PresupuestosDao) {

    val presupuestos: Flow<List<PresupuestosModel>> = presupuestosDao.getPresupuestos()
        .map { items ->
            items.map {
                PresupuestosModel(
                    it.id,
                    it.fecha,
                    it.direccion,
                    it.nombre,
                    it.telefono,
                    it.concepto,
                    it.precioConcepto,
                    it.precioBase,
                    it.precio,
                    it.iva,
                    it.imagenesPresupuesto
                )
            }
        }

    suspend fun add(presupuestosModel: PresupuestosModel) {
        presupuestosDao.addPresupuesto(presupuestosModel.toData())
    }

    suspend fun update(presupuestosModel: PresupuestosModel) {
        presupuestosDao.updatePresupuesto(presupuestosModel.toData())
    }

    suspend fun delete(presupuestosModel: PresupuestosModel) {
        presupuestosDao.deletePresupuesto(presupuestosModel.toData())
    }

    suspend fun getOnePresupuestoById(id: Int): PresupuestosModel? {
        val presupuestoEntity = presupuestosDao.getPresupuestoById(id)
        return presupuestoEntity.toPresupuestosModel()
    }
}

fun PresupuestosModel.toData(): PresupuestoEntity {
    return PresupuestoEntity(
        this.id,
        this.fecha,
        this.direccion,
        this.nombre,
        this.telefono,
        this.concepto,
        this.precioConcepto,
        this.precioBase,
        this.precio,
        this.iva,
        this.imagenesPresupuesto
    )
}