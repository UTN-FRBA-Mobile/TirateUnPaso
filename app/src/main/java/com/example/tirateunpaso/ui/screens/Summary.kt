package com.example.tirateunpaso.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp


data class BarChartData(val category: String, val value: Int)

// Función para generar datos de ejemplo
fun generateMockBarChartData(): List<BarChartData> {
    return listOf(
        BarChartData("Category 1", 20),
        BarChartData("Category 2", 35),
        BarChartData("Category 3", 15)
    )
}

// Gráfico en horizontal
@Composable
fun BarChartH(data: List<BarChartData>) {
    // Obtener el valor máximo para ajustar la anchura de la barra
    val maxValue = data.maxOfOrNull { it.value } ?: 1 // Evitar división por cero

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            data.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = item.category,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // Box con ancho dinámico calculado
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .widthIn(max = 160.dp) // Ancho máximo de la barra
                            .background(Color.Blue.copy(alpha = 0.7f))
                            .fillMaxWidth(fraction = item.value.toFloat() / maxValue.toFloat())
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.value.toString(),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

/// Gráfico en vertical
@Composable
fun BarChartV(data: List<BarChartData>) {
    // Obtener el valor máximo para ajustar la altura de la barra
    val maxValue = data.maxOfOrNull { it.value } ?: 1 // Evitar división por cero

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Bottom, // Alinea el contenido al fondo de la Card
            modifier = Modifier.padding(16.dp)
        ) {
            data.forEach { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.Bottom // Alinea el contenido de la columna en la parte inferior
                ) {
                    Text(
                        text = item.value.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .heightIn(max = 200.dp) // Altura máxima de la barra
                            .background(Color.Blue.copy(alpha = 0.7f))
                            .fillMaxHeight(fraction = item.value.toFloat() / maxValue.toFloat())
                    )

                    Text(
                        text = item.category,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}


@Composable
fun FilterScreen(viewModel: FilterViewModel = viewModel()) {
    val data = remember { generateMockBarChartData() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        BarChartH(data = data)
        BarChartV(data = data)
    }
}

class FilterViewModel : ViewModel()

class FilterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FilterScreen()
            }
        }
    }
}