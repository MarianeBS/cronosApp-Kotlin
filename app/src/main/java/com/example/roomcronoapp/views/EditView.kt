package com.example.roomcronoapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roomcronoapp.R
import com.example.roomcronoapp.components.CircleButton
import com.example.roomcronoapp.components.MainIconButton
import com.example.roomcronoapp.components.MainTextField
import com.example.roomcronoapp.components.MainTitle
import com.example.roomcronoapp.components.formatTiempo
import com.example.roomcronoapp.model.Cronos
import com.example.roomcronoapp.ui.theme.pinkthird
import com.example.roomcronoapp.ui.theme.pinksecond
import com.example.roomcronoapp.ui.theme.pinkprimary
import com.example.roomcronoapp.viewModels.CronometroViewModel
import com.example.roomcronoapp.viewModels.CronosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditView(
    navController: NavController,
    cronometroVM: CronometroViewModel,
    cronosVM: CronosViewModel,
    id: Long
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "EDITAR CRONOMETRO") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = pinkthird
                ),
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { it ->
        ContentEditView(it, navController, cronometroVM, cronosVM, id)
    }
}

@Composable
fun ContentEditView(
    it: PaddingValues,
    navController: NavController,
    cronometroVM: CronometroViewModel,
    cronosVM: CronosViewModel,
    id:Long
) {

    val state = cronometroVM.state

    LaunchedEffect(state.cronometroActivo) {
        cronometroVM.cronos()
    }

    LaunchedEffect(Unit){
        cronometroVM.getCronoById(id)
    }

    Spacer(modifier = Modifier.height(30.dp))

    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(pinkthird),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = formatTiempo(cronometroVM.tiempo),
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = pinkprimary
        )


        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            // iniciar
            CircleButton(
                icon = painterResource(id = R.drawable.play),
                enabled = !state.cronometroActivo
            ) {
                cronometroVM.iniciar()
            }
            // pausar
            CircleButton(
                icon = painterResource(id = R.drawable.pausa),
                enabled = state.cronometroActivo
            ) {
                cronometroVM.pausar()
            }
        }
            MainTextField(
                value = state.title,
                onValueChange = { cronometroVM.onValue(it) },
                label = "Título"
            )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Ajuste o padding conforme necessário
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp), // Ajuste o padding conforme necessário
                colors = ButtonDefaults.buttonColors(containerColor = pinksecond, contentColor = pinkprimary),
                onClick = {
                    cronosVM.updateCrono(
                        Cronos(
                            id = id,
                            title = state.title,
                            crono = cronometroVM.tiempo
                        )
                    )
                    navController.popBackStack()
                }
            ) {
                Text(text = "Editar", color = pinkprimary)
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp), // Ajuste o padding conforme necessário
                colors = ButtonDefaults.buttonColors(containerColor = pinksecond, contentColor = pinkprimary),
                onClick = {
                    cronosVM.deleteCrono(
                        Cronos(
                            id = id,
                            title = state.title,
                            crono = cronometroVM.tiempo
                        )
                    )
                }
            ) {
                Text(text = "Excluir", color = pinkprimary)
            }
        }

        DisposableEffect(Unit){
            onDispose {
                cronometroVM.detener()
            }
        }


    }
}

fun oi(navController: NavController){
    navController.navigate("Home")
}