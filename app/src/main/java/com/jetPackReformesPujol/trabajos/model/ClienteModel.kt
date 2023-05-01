package com.jetPackReformesPujol.trabajos.model

data class ClienteModel(
    val id: Int = System.currentTimeMillis().hashCode(),
    var campingParcela: String,
    var fecha: String? = null,
    var nombreCliente: String? = null,
    var telefono: Long? = null,
    var precio: Int? = null,
    var pagaisenal: Int? = null,
    val imagenesCliente: String? = null

        )