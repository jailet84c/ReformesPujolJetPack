package com.jetPackReformesPujol.presupuestos.model

data class PresupuestosModel(
    val id: Int = System.currentTimeMillis().hashCode(),
    val fecha: String = "",
    val direccion: String = "",
    val nombre: String = "",
    val telefono: Long = 0L,
    val concepto: String = "",
    val precioConcepto: Float = 0F,
    val precioBase: Float = 0F,
    val iva: Float = 0F,
    val precio: Float = 0F,
    val imagenesPresupuesto: String = ""
)