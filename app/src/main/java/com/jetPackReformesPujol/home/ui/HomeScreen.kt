package com.jetPackReformesPujol.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.jetPackReformesPujol.R
import com.jetPackReformesPujol.Utils.MyTopAppBar
import com.jetPackReformesPujol.model.Routes.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContentHome(navigationController: NavHostController,navigateUp: () -> Unit) {
    Scaffold(topBar = { MyTopAppBar(navigateUp) }) {
        ConstraintLayout() {
            val (logo, btPre, btCam, btTra, btMat, btIns, btWeb) = createRefs()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFDFDFDF))
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(btPre.top, margin = 16.dp)
                    }
                    .constrainAs(btPre) {
                        top.linkTo(logo.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(btTra.start)
                    }
                    .constrainAs(btCam) {
                        top.linkTo(btPre.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(btMat.start)
                    }
                    .constrainAs(btTra) {
                        top.linkTo(logo.bottom, margin = 20.dp)
                        start.linkTo(btPre.end)
                        end.linkTo(parent.end)
                    }
                    .constrainAs(btMat) {
                        top.linkTo(btTra.bottom, margin = 20.dp)
                        start.linkTo(btCam.start)
                        end.linkTo(parent.end)
                    }
                    .constrainAs(btIns) {
                        top.linkTo(btCam.bottom, margin = 20.dp)
                        start.linkTo(parent.start, margin = 20.dp)
                        end.linkTo(btWeb.start, margin = 20.dp)
                    }
                    .constrainAs(btWeb) {
                        top.linkTo(btMat.bottom, margin = 20.dp)
                        start.linkTo(btIns.end, margin = 20.dp)
                        end.linkTo(parent.end, margin = 20.dp)
                    }
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                ) {
                    Image(
                        painterResource(id = R.drawable.reformespujol),
                        contentDescription = "logo",
                        modifier = Modifier.size(170.dp))
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 30.dp, top = 80.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {navigationController.navigate(PresupuestosScreen.route)},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Gray
                        ),
                        border = BorderStroke(1.dp, Color(0xFF01155C)),
                        modifier = Modifier
                            .size(140.dp)

                    ) {
                            Icon(
                                painterResource(id = R.drawable.presupuestos),
                                contentDescription = "presupuestos icono",
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Presupuestos",
                                fontSize = 10.sp
                            )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = { navigationController.navigate(CampingsScreen.route)},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Gray
                        ),
                        border = BorderStroke(1.dp, Color(0xFF01155C)),
                        modifier = Modifier
                            .size(140.dp)

                    ) {
                        Icon(
                            painterResource(id = com.jetPackReformesPujol.R.drawable.campings),
                            contentDescription = "campings icono",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = "Campings")
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp, top = 80.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navigationController.navigate(TrabajosScreen.route)},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Gray),
                        border = BorderStroke(1.dp, Color(0xFF01155C)),
                        modifier = Modifier
                            .size(140.dp)

                    ) {
                        Icon(
                            painterResource(id = com.jetPackReformesPujol.
                            R.drawable.trabajos),
                            contentDescription = "trabajos icono",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = "Trabajos")

                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = { navigationController.navigate(MaterialesScreen.route)},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Gray
                        ),
                        border = BorderStroke(1.dp, Color(0xFF01155C)),
                        modifier = Modifier
                            .size(140.dp)

                    ) {
                        Icon(
                            painterResource(id = R.drawable.materiales),
                            contentDescription = "materiales icono",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = "Materiales")
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                ) {
                    Icon(
                        painter = rememberAsyncImagePainter(InstagramIcon()),
                        contentDescription = "Instagram Icon",
                        modifier = Modifier
                            .padding(8.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.web),
                        contentDescription = "web",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InstagramIcon() {
    val instaColors = listOf(Color.Yellow, Color.Red, Color.Magenta)
    Canvas(
        modifier = Modifier
            .size(80.dp)
            .padding(16.dp)
    ) {
        drawRoundRect(
            brush = Brush.linearGradient(colors = instaColors),
            cornerRadius = CornerRadius(60f, 60f),
            style = Stroke(width = 15f, cap = StrokeCap.Round)
        )
        drawCircle(
            brush = Brush.linearGradient(colors = instaColors),
            radius = 45f,
            style = Stroke(width = 15f, cap = StrokeCap.Round)
        )
        drawCircle(
            brush = Brush.linearGradient(colors = instaColors),
            radius = 13f,
            center = Offset(this.size.width * .80f, this.size.height * 0.20f)
        )
    }
}

