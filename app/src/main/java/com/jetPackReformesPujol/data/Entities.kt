package com.jetPackReformesPujol.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MaterialesEntity(
    @PrimaryKey
    val id: Int,
    val material: String
)

@Entity
data class ClienteEntity(
    @PrimaryKey
    val id: Int,
    val campingParcela: String,
    val fecha: String,
    val nombreCliente: String,
    val telefono: Long,
    val precio: Int,
    val pagaisenal: Int,
    val imagenesCliente: String
)

@Entity
data class PresupuestoEntity(
    @PrimaryKey
    val id: Int,
    val fecha: String,
    val direccion: String,
    val nombre: String,
    val telefono: Long,
    val concepto: String,
    val precioConcepto: Float,
    val precioBase: Float,
    val iva: Float,
    val precio: Float,
    val imagenesPresupuesto: String
)