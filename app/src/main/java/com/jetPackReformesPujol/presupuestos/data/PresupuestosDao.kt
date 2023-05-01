package com.jetPackReformesPujol.presupuestos.data

import androidx.room.*
import com.jetPackReformesPujol.data.PresupuestoEntity
import com.jetPackReformesPujol.presupuestos.model.PresupuestosModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PresupuestosDao {

    @Query("SELECT * from PresupuestoEntity")
    fun getPresupuestos(): Flow<List<PresupuestoEntity>>

    @Query("SELECT * from PresupuestoEntity WHERE id = :id")
    fun getPresupuestoById(id: Int):PresupuestoEntity

    @Insert
    suspend fun addPresupuesto(item: PresupuestoEntity)

    @Update
    suspend fun updatePresupuesto(item: PresupuestoEntity)

    @Delete
    suspend fun deletePresupuesto(item: PresupuestoEntity)

}

fun PresupuestoEntity?.toPresupuestosModel(): PresupuestosModel? {
    if (this == null) return null

    return PresupuestosModel(
        id = this.id,
        fecha = this.fecha,
        direccion = this.direccion,
        nombre = this.nombre,
        telefono = this.telefono,
        concepto = this.concepto,
        precioConcepto = this.precioConcepto,
        precioBase = this.precioBase,
        iva = this.iva,
        precio = this.precio,
        imagenesPresupuesto = this.imagenesPresupuesto
    )
}