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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.cos
import kotlin.math.sin




data class GraphData(val category: String, val value: Int)

// Función para generar datos de ejemplo
fun generateGraphData(): List<GraphData> {
    return listOf(
        GraphData("Categoría 1", 20),
        GraphData("Categoría 2", 35),
        GraphData("Categoría 3", 15)
    )
}

@Composable
fun BarChartH(data: List<GraphData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(4.dp),
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            data.forEach { item ->
                val animatedWidth by animateFloatAsState(
                    targetValue = item.value.toFloat() / maxValue.toFloat(), label = ""
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
                                    colors = listOf(
                                        Color(0xFF42A5F5).copy(alpha = 0.7f), // Color con transparencia
                                        Color(0xFF1976D2).copy(alpha = 0.7f)  // Color con transparencia
                                    )
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
fun BarChartV(data: List<GraphData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(4.dp),
            )
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(16.dp)
        ) {
            data.forEach { item ->
                val animatedHeight by animateFloatAsState(
                    targetValue = item.value.toFloat() / maxValue.toFloat(), label = ""
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
                                    colors = listOf(
                                        Color(0xFF1976D2).copy(alpha = 0.7f), // Color con transparencia
                                        Color(0xFF42A5F5).copy(alpha = 0.7f)  // Color con transparencia
                                    )
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
fun PieChart(data: List<GraphData>) {
    val total = data.sumOf { it.value.toDouble() }
    val sweepAngles = data.map { it.value.toFloat() / total.toFloat() * 360f }
    val colors = listOf(
        Color(0xFF1976D2).copy(alpha = 0.7f),
        Color(0xFF42A5F5).copy(alpha = 0.7f),
        Color(0xFF01579B).copy(alpha = 0.7f)
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(4.dp),
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp) // padding interno en todas las direcciones
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                // .aspectRatio(1f) // Circular pero no entra bien en la card, mejor dejarlo asi
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    var startAngle = 0f
                    data.forEachIndexed { index, item ->
                        val sweepAngle = sweepAngles[index]
                        drawArc(
                            color = colors[index % colors.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true,
                            size = Size(size.width, size.height)
                        )
                        // funciona...
                        val angle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                        val x = (size.width / 2 + (size.width / 4) * cos(angle)).toFloat()
                        val y = (size.height / 2 + (size.height / 4) * sin(angle)).toFloat()

                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                item.value.toString(),
                                x,
                                y - 20, // Posición del valor
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    textSize = 40f
                                    isAntiAlias = true
                                }
                            )
                            drawText(
                                item.category,
                                x,
                                y + 25, // Posición de categoría
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    textSize = 30f // Título un toque más chico que el valor
                                    isAntiAlias = true
                                }
                            )
                        }
                        startAngle += sweepAngle
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                data.forEachIndexed { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(colors[index % colors.size])
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "${item.category}: ${item.value}")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun GraphScreen(viewModel: GraphViewModel = viewModel()) {
    val data = remember { generateGraphData() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        BarChartH(data = data)
        BarChartV(data = data)
        PieChart(data = data)
    }
}

class GraphViewModel : ViewModel()

class GraphActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GraphScreen()
            }
        }
    }
}