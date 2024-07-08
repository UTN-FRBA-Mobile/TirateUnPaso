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

// Data class para representar los datos sin procesar
data class RawData(val date: Date, val value: Int)

// Objeto para generar datos de prueba
object DataGenerator {
    fun generateRawGraphData(): List<RawData> {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val data = mutableListOf<RawData>()

        // Generar 35 días de datos
        repeat(35) {
            val date = today.clone() as Calendar
            date.add(Calendar.DAY_OF_YEAR, -it) // Retroceder i días desde hoy

            // Determinar el valor de cada día para testear
            val value = when (it) {
                0, 6 -> 250  // Hoy y hoy + 6 valor 250
                1 -> 0
                7, 14 -> 100 // Hoy + 7 y hoy + 14 valor 100
                else -> 1000 // 1000 en todos los demás días
            }

            data.add(RawData(date.time, value))
        }

        return data
    }
}

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