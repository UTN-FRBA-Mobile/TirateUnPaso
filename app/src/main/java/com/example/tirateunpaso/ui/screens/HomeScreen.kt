package com.example.tirateunpaso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tirateunpaso.ui.components.BottomButton
import com.example.tirateunpaso.ui.components.CircularButton
import com.example.tirateunpaso.ui.components.HeaderText
import com.example.tirateunpaso.ui.values
import com.example.tirateunpaso.viewmodel.StepCounterVM

@Composable
fun HomeScreen(
    onLogoutClick:() -> Unit,
    onStatisticsClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    stepCounterVM : StepCounterVM
) {
    val uiState by stepCounterVM.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(values.defaultPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onLogoutClick,
            modifier = Modifier.align(alignment = Alignment.Start),
        ){
            Text(
                text = "Cerrar sesión",
                fontSize = values.fontsize
            )
        }
        HeaderText(
            text = "Mi actividad hoy",
            modifier = Modifier.padding(vertical = values.defaultPadding)
        )
        Spacer(modifier = Modifier.height(values.defaultSpacing))
        CircularButton(
            onClick = onStatisticsClick,
            number = uiState.stepCount,
            measure = "pasos dados"
        )
        Spacer(modifier = Modifier.height(values.defaultSpacing))
        CircularButton(
            onClick = onStatisticsClick,
            number = uiState.stepCount/1449,
            measure = "km recorridos"
        )
    }
    BottomButton(
        alignment = Alignment.BottomStart,
        onClick = onAchievementsClick,
        text = "Logros"
    )
    BottomButton(
        alignment = Alignment.BottomEnd,
        onClick = onStatisticsClick,
        text = "Estadísticas"
    )

}

/*
@Preview(showSystemUi = true)
@Composable
fun DefaultHomePreview() {
    HomeScreen({},{},{})
}
 */