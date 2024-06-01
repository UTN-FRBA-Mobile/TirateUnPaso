package com.example.tirateunpaso.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.cos
import kotlin.math.sin

// ViewModel
data class GraphData(val category: String, val value: Int)

class GraphViewModel : ViewModel() {
    private val _graphData = MutableStateFlow<List<GraphData>>(emptyList())
    val graphData: StateFlow<List<GraphData>> = _graphData

    init {
        generateGraphData()
    }

    private fun generateGraphData() {
        viewModelScope.launch {
            val data = listOf(
                GraphData("Categoría 1", 20),
                GraphData("Categoría 2", 35),
                GraphData("Categoría 3", 15)
            )
            _graphData.value = data
        }
    }
}

// Pantalla
@Composable
fun BarChartH(data: List<GraphData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        // Tarjeta del gráfico incluyendo el título
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // Card celeste
        ) {
            Column {
                // Título del gráfico
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)  // Altura del título
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF42A5F5),
                                    Color(0xFF1976D2)
                                )
                            ),
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Gráfico de barras horizontal",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // Contenido del gráfico
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
                                                Color(0xFF42A5F5),
                                                Color(0xFF1976D2)
                                            )
                                        ),
                                        shape = RoundedCornerShape(8.dp)  // Barras redondeadas
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
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun BarChartV(data: List<GraphData>) {
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        // Tarjeta del gráfico incluyendo el título
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // Card celeste
        ) {
            Column {
                // Título del gráfico
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)  // Altura del título
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF42A5F5),
                                    Color(0xFF1976D2)
                                )
                            ),
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Gráfico de barras vertical",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // Contenido del gráfico
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
                                                Color(0xFF1976D2),
                                                Color(0xFF42A5F5)
                                            )
                                        ),
                                        shape = RoundedCornerShape(8.dp)  // Barras redondeadas
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
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun PieChart(data: List<GraphData>) {
    val total = data.sumOf { it.value.toDouble() }
    val sweepAngles = data.map { it.value.toFloat() / total.toFloat() * 360f }
    val colors = listOf(
        Color(0xFF1976D2),
        Color(0xFF42A5F5),
        Color(0xFF01579B)
    )

    // Tarjeta del gráfico incluyendo el título
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(8.dp),
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // Card celeste
    ) {
        Column {
            // Título del gráfico
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp) // Altura del título
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF42A5F5),
                                Color(0xFF1976D2)
                            )
                        ),
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Gráfico de torta",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp) // padding interno en todas las direcciones
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
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

                // Leyendas
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
                            Text(text = "${item.category}: ${item.value}",
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun GraphScreen(viewModel: GraphViewModel = viewModel()) {
    val data by viewModel.graphData.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()) // esto es para que la pantalla tenga scroll
    ) {
        BarChartH(data = data)
        BarChartV(data = data)
        PieChart(data = data)
    }
}

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