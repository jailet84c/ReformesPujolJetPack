package com.jetPackReformesPujol

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jetPackReformesPujol.AppDestinations.CAMPINGS_DETAIL_ID_KEY
import com.jetPackReformesPujol.AppDestinations.CLIENTES_DETAIL_ID_KEY
import com.jetPackReformesPujol.AppDestinations.PHOTO_DETAIL_ID_KEY
import com.jetPackReformesPujol.AppDestinations.PRESUPUESTO_ID_KEY
import com.jetPackReformesPujol.campings.ui.CampingDetail
import com.jetPackReformesPujol.campings.ui.CampingsScreen
import com.jetPackReformesPujol.home.ui.MainContentHome
import com.jetPackReformesPujol.login.ui.LoginViewModel
import com.jetPackReformesPujol.login.ui.MainContentLogin
import com.jetPackReformesPujol.materiales.ui.MainContentMateriales
import com.jetPackReformesPujol.materiales.ui.MaterialesViewModel
import com.jetPackReformesPujol.model.Routes
import com.jetPackReformesPujol.presupuestos.ui.EmptyPresupuestoMain
import com.jetPackReformesPujol.presupuestos.ui.MainContentPresupuesto
import com.jetPackReformesPujol.presupuestos.ui.PresupuestoMain
import com.jetPackReformesPujol.presupuestos.ui.PresupuestosViewModel
import com.jetPackReformesPujol.trabajos.ui.MainContentDetailFotoTrabajo
import com.jetPackReformesPujol.trabajos.ui.MainContentTrabajos
import com.jetPackReformesPujol.trabajos.ui.TrabajoDetail
import com.jetPackReformesPujol.trabajos.ui.TrabajosViewModel

object AppDestinations {
    const val CAMPINGS_DETAIL_ID_KEY = "campingId"
    const val CLIENTES_DETAIL_ID_KEY = "clienteId"
    const val PHOTO_DETAIL_ID_KEY = "photoId"
    const val PRESUPUESTO_ID_KEY = "presupuestoId"
}

@Composable
fun AppNavigation(navController: NavHostController) {

    val actions = remember(navController) { AppActions(navController) }

    NavHost(
        navController = navController,
        startDestination = Routes.LoginScreen.route
    ) {
        composable(Routes.LoginScreen.route) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            MainContentLogin(navController, loginViewModel)
        }
        composable(Routes.CampingsScreen.route) {
            CampingsScreen(selectedCamping = actions.selectedCamping, actions.navigateUp)
        }
        composable(
            "${Routes.CampingDetail.route}/{${CAMPINGS_DETAIL_ID_KEY}}",
            arguments = listOf(
                navArgument(CAMPINGS_DETAIL_ID_KEY) { type = NavType.IntType })
        ) { backStackEntry ->
            val argumentsCamping = requireNotNull(backStackEntry.arguments)
            CampingDetail(
                campingId = argumentsCamping.getInt(CAMPINGS_DETAIL_ID_KEY),
                navigateUp = actions.navigateUp
            )
        }
        composable(Routes.TrabajosScreen.route) {
            val viewModelTrabajos = hiltViewModel<TrabajosViewModel>()
            MainContentTrabajos(
                actions.selectedTrabajo,
                navController,
                viewModelTrabajos,
                actions.navigateUp
            )
        }
        composable(
            "${Routes.TrabajoDetail.route}/{${CLIENTES_DETAIL_ID_KEY}}",
            arguments = listOf(navArgument(CLIENTES_DETAIL_ID_KEY) {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val viewModelTrabajos = hiltViewModel<TrabajosViewModel>()
            TrabajoDetail(
                actions.selectedPhotoId,
                clienteId = backStackEntry.arguments!!.getString(CLIENTES_DETAIL_ID_KEY)!!,
                navigateUp = actions.navigateUp, navController,
                trabajosViewModel = viewModelTrabajos
            )
        }
        composable(
            "${Routes.DetailFotoTrabajo.route}/{${PHOTO_DETAIL_ID_KEY}}",
            arguments = listOf(navArgument(PHOTO_DETAIL_ID_KEY) {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val viewModelTrabajos = hiltViewModel<TrabajosViewModel>()
            MainContentDetailFotoTrabajo(
                photoId = backStackEntry.arguments!!.getString(PHOTO_DETAIL_ID_KEY)!!,
                navigateUp = actions.navigateUp, navController,
                trabajosViewModel = viewModelTrabajos
            )
        }
        composable(Routes.HomeScreen.route) {
            MainContentHome(
                navController,
                actions.navigateUp
            )
        }
        composable(Routes.PresupuestosScreen.route) {
            //Instanciar viewModel con Hilt
            val viewModelPresupuestos = hiltViewModel<PresupuestosViewModel>()
            MainContentPresupuesto(
                actions.selectedPresupuesto,
                viewModelPresupuestos,
                navController,
                actions.navigateUp
            )
        }

        composable(Routes.EmptyPresupuesto.route) {
            //Instanciar viewModel con Hilt
            val viewModelPresupuestos = hiltViewModel<PresupuestosViewModel>()
            EmptyPresupuestoMain(
                viewModelPresupuestos,
                navController,
                actions.navigateUp
            )
        }

        composable(
            "${Routes.Presupuesto.route}/{${PRESUPUESTO_ID_KEY}}?",
            arguments = listOf(navArgument(PRESUPUESTO_ID_KEY)  {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            //Instanciar viewModel con Hilt
            val viewModelPresupuestos = hiltViewModel<PresupuestosViewModel>()
            // Obtener el valor del ID del presupuesto o establecer un valor predeterminado si es nulo
            val presupuestoId = backStackEntry.arguments?.getInt(PRESUPUESTO_ID_KEY) ?: -1
            PresupuestoMain(
                presupuestoId,
                viewModelPresupuestos,
                navController,
                actions.navigateUp
            )
        }

        composable(Routes.MaterialesScreen.route) {
            //Instanciar viewModel con Hilt
            val viewModelMateriales = hiltViewModel<MaterialesViewModel>()
            MainContentMateriales(navController, viewModelMateriales, actions.navigateUp)
        }
    }
}

private class AppActions(navController: NavHostController) {

    val selectedCamping: (Int) -> Unit = { campingId: Int ->
        navController.navigate("${Routes.CampingDetail.route}/$campingId")
    }
    val selectedTrabajo: (String) -> Unit = { clienteId: String ->
        navController.navigate("${Routes.TrabajoDetail.route}/$clienteId")
    }

    val selectedPhotoId: (String) -> Unit = { photoId: String ->
        navController.navigate("${Routes.DetailFotoTrabajo.route}/$photoId")
    }

    val selectedPresupuesto: (Int) -> Unit = { presupuestoId: Int ->
        navController.navigate("${Routes.Presupuesto.route}/$presupuestoId")
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}