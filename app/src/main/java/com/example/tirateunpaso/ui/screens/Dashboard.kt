package com.example.tirateunpaso.ui.screens

import TirateUnPasoTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.*

// Objeto para generar datos de prueba
@Composable
fun GraphScreen(viewModel: GraphViewModel = viewModel()) {
    val data by viewModel.graphData.collectAsState()
    val selectedGraph by viewModel.selectedGraph.collectAsState()
    val stepsToday by viewModel.stepsToday.collectAsState()
    val lastSevenDaysData by viewModel.lastSevenDaysData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // para que haya scroll
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepsCard(stepsToday = stepsToday)

        GraphContainer(
            selectedGraph = selectedGraph,
            data = data,
            lastSevenDaysData = lastSevenDaysData,
            onGraphSelected = { graphType ->
                viewModel.selectGraph(graphType)
            }
        )

        CalendarScreen()
    }
}


// Actividad que muestra la pantalla (para testear)
class GraphActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TirateUnPasoTheme {
                GraphScreen()
            }
        }
    }
}