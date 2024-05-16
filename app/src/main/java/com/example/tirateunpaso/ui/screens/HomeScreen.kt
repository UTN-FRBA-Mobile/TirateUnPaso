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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tirateunpaso.ui.components.CircularButton
import com.example.tirateunpaso.ui.components.HeaderText
import com.example.tirateunpaso.ui.values

@Composable
fun HomeScreen(onLogoutClick:() -> Unit) {
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
            onClick = {},
            number = 1000,
            measure = "pasos dados"
        )
        Spacer(modifier = Modifier.height(values.defaultSpacing))
        CircularButton(
            onClick = {},
            number = 1000,
            measure = "km recorridos"
        )

    }
}

@Preview
@Composable
fun DefaultHomePreview() {
    HomeScreen({})
}