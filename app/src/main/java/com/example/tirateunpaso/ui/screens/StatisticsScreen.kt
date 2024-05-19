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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import com.example.tirateunpaso.ui.components.Graphic
import com.example.tirateunpaso.ui.components.HeaderText
import com.example.tirateunpaso.ui.values

@Composable
fun StatisticsScreen(
    onHomeClick:() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(values.defaultPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onHomeClick,
            modifier = Modifier.align(alignment = Alignment.Start),
        ){
            Text(
                text = "Volver",
                fontSize = values.fontsize
            )
        }
        HeaderText(
            text = "Mis estadísticas",
            modifier = Modifier.padding(vertical = values.defaultPadding)
        )
        Spacer(modifier = Modifier.height(values.defaultSpacing))
        Graphic(
            listOf(
                Offset(1.0f, 100.0f),
                Offset(2.0f, 1200.0f),
                Offset(3.0f, 500.0f),
                Offset(5.0f, 800.0f),
                Offset(8.0f, 300.0f),
            ),
            yMinDefaultValue = 0.0f,
        )
        Spacer(modifier = Modifier.height(values.defaultSpacing))
        Graphic(
            listOf(
                Offset(1.0f, 100.0f),
                Offset(2.0f, 1200.0f),
                Offset(3.0f, 500.0f),
                Offset(5.0f, 800.0f),
                Offset(8.0f, 300.0f),
            ),
            yMinDefaultValue = 0.0f,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultStatisticsScreenPreview() {
    StatisticsScreen({})
}