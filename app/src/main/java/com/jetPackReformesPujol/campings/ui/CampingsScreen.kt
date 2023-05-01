package com.jetPackReformesPujol.campings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jetPackReformesPujol.Utils.MyTopAppBar
import com.jetPackReformesPujol.campings.data.CampingRepo
import com.jetPackReformesPujol.campings.model.CampingModel

@Composable
fun CampingsScreen(selectedCamping: (Int) -> Unit, navigateUp: () -> Unit) {
    Scaffold(topBar = { MyTopAppBar(navigateUp) }) {
        Box(modifier = Modifier.fillMaxSize()) {
            CampingsListBody(selectedCamping)
        }
    }
}

@Composable
fun CampingsListBody(selectedCamping: (Int) -> Unit) {
    val scrollState = rememberLazyListState()
    val context = LocalContext.current
    val campings: List<CampingModel> = CampingRepo.getCampingsList(context)

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = scrollState,
        contentPadding = PaddingValues(16.dp)
    ) {
        items(campings) { camping ->
            CampingRow(camping, selectedCamping)
        }
    }
}
@Composable
fun CampingRow(camping: CampingModel, selectedCamping: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = { selectedCamping(camping.campingId) })
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = camping.image),
                contentDescription = null,
                modifier = Modifier
                    .size(130.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = camping.name,
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = camping.description,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}
