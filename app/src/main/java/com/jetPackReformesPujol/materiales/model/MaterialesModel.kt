package com.jetPackReformesPujol.materiales.model

data class MaterialesModel(val id:Int = System.currentTimeMillis().hashCode(), val material:String)