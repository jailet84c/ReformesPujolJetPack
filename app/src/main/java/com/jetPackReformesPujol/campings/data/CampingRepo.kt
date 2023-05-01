package com.jetPackReformesPujol.campings.data

import android.content.Context
import com.jetPackReformesPujol.R
import com.jetPackReformesPujol.campings.model.CampingModel

object CampingRepo {

    fun getCamping(campingId: Int, context: Context): CampingModel =
        getCampingsList(context).find { it.campingId == campingId }!!

    fun getCampingsList(context: Context) = listOf(
        CampingModel(
            1,
            "Berga Resort",
            "Salida 95, E-9 / C-16, km. 96,3, 08600 Berga, Barcelona",
            R.drawable.logobergaresort,
            R.drawable.mapabergaresort
        ),
        CampingModel(
            2,
            "Camping Kanguro",
            "N-II, 08395 Sant Pol de Mar, Barcelona",
            R.drawable.logokanguro,
            R.drawable.kanguro
        ),
        CampingModel(
            3,
            "Aqua Alba",
            "Plaça Sector Aqualba, 08474, Barcelona",
            R.drawable.logogualba,
            R.drawable.mapagualba
        ),
        CampingModel(
            4,
            "Camping Mas Josep",
            "Camí Ral, 2, 17246 Santa Cristina d'Aro, Girona",
            R.drawable.logomassantjosep,
            R.drawable.massantjosep
        ),
        CampingModel(
            5,
            "Camping Roca Grossa",
            "N-II, Km. 665",
            R.drawable.rocagrossalogo,
            R.drawable.maparocagrossa
        ),
        CampingModel(
            6,
            "Camping Tossa",
            "Ctra. Llagostera, Km 13, 17320 Tossa de Mar, Girona",
            R.drawable.mapatossa,
            R.drawable.mapatossa

        )
    )
}