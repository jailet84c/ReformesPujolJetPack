package com.jetPackReformesPujol.model

sealed class Routes(val route: String) {
    object LoginScreen:Routes("login")
    object HomeScreen:Routes("home")
    object PresupuestosScreen:Routes("presupuesto")
    object Presupuesto:Routes("detallePresupuesto")
    object EmptyPresupuesto:Routes("PresupuestoVacio")
    object TrabajosScreen:Routes("trabajos")
    object TrabajoDetail:Routes("trabajoDetail")
    object DetailFotoTrabajo:Routes("detailFotoTrabajo")
    object MaterialesScreen:Routes("materiales")
    object CampingsScreen:Routes("campings")
    object CampingDetail:Routes("camping")
}