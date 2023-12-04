package com.example.roomcronoapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
fun AddView(navController: NavController, cronometroVM: CronometroViewModel, cronosVM: CronosViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "CRIAR CRONOMETRO") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = pinkthird,
                    titleContentColor = pinkprimary
                ),
                navigationIcon = {
                    MainIconButton(icon = Icons.Default.ArrowBack) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { it ->
        ContentAddView(it, navController, cronometroVM, cronosVM)
    }
}

@Composable
fun ContentAddView(
    it: PaddingValues,
    navController: NavController,
    cronometroVM: CronometroViewModel,
    cronosVM: CronosViewModel
) {

    val state = cronometroVM.state

    LaunchedEffect(state.cronometroActivo) {
        cronometroVM.cronos()
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

            // detener
            CircleButton(
                icon = painterResource(id = R.drawable.stop),
                enabled = !state.cronometroActivo
            ) {
                cronometroVM.detener()
            }

            // mostrar guardar
            CircleButton(
                icon = painterResource(id = R.drawable.save),
                enabled = state.showSaveButton
            ) {
                cronometroVM.showTextField()
            }

        }

        if (state.showTextField) {
            MainTextField(
                value = state.title,
                onValueChange = { cronometroVM.onValue(it) },
                label = "Título"
            )

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = pinksecond, contentColor = pinkprimary),
                onClick = {
                cronosVM.addCrono(
                    Cronos(
                        title = state.title,
                        crono = cronometroVM.tiempo
                    )
                )

                navController.popBackStack()

            }) {
                Text(text = "Criar")
            }
            DisposableEffect(Unit){
                onDispose {
                    cronometroVM.detener()
                }
            }

        }
    }
}