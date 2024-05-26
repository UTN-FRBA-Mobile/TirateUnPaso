package com.example.tirateunpaso.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class BarChartData(val category: String, val value: Int)

// Función para generar datos de ejemplo
fun generateMockBarChartData(): List<BarChartData> {
    return listOf(
        BarChartData("Category 1", 20),
        BarChartData("Category 2", 35),
        BarChartData("Category 3", 15)
    )
}

@Composable
fun BarChartH(data: List<BarChartData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp, // Ajusta la elevación aquí
                shape = RoundedCornerShape(4.dp), // Forma de la sombra
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            data.forEach { item ->
                val animatedWidth by animateFloatAsState(
                    targetValue = item.value.toFloat() / maxValue.toFloat()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = item.category,
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .widthIn(max = 160.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF1976D2), Color(0xFF42A5F5)),
                                    startX = 0f,
                                    endX = 1000f
                                ),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .fillMaxWidth(fraction = animatedWidth)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.value.toString(),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BarChartV(data: List<BarChartData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp, // Ajusta la elevación aquí
                shape = RoundedCornerShape(4.dp), // Forma de la sombra
            )
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(16.dp)
        ) {
            data.forEach { item ->
                val animatedHeight by animateFloatAsState(
                    targetValue = item.value.toFloat() / maxValue.toFloat()
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = item.value.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .heightIn(max = 200.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF1976D2), Color(0xFF42A5F5)),
                                    startY = 0f,
                                    endY = 1000f
                                ),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .fillMaxHeight(fraction = animatedHeight)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.category,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
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