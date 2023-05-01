package com.jetPackReformesPujol.materiales.ui

import com.jetPackReformesPujol.materiales.model.MaterialesModel

sealed interface MaterialesUistate {
    object Loading: MaterialesUistate
    data class Error(val throwable: Throwable):MaterialesUistate
    data class Succes(val materiales:List<MaterialesModel>) : MaterialesUistate
}