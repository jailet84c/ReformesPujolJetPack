package com.jetPackReformesPujol.trabajos.ui

import com.jetPackReformesPujol.trabajos.model.ClienteModel

sealed interface TrabajosUistate {
    object LoadingT: TrabajosUistate
    data class ErrorT(val throwable: Throwable): TrabajosUistate
    data class SuccesT(val clientes:List<ClienteModel>) : TrabajosUistate
}