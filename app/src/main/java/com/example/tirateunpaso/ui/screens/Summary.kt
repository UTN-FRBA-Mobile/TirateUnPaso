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
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

import java.text.SimpleDateFormat
import java.util.*

// ViewModel
data class GraphData(val category: String, val value: Int)

enum class GraphType {
    HORIZONTAL, VERTICAL, TORTA
}

class GraphViewModel : ViewModel() {
    private val _graphData = MutableStateFlow<List<GraphData>>(emptyList())
    val graphData: StateFlow<List<GraphData>> = _graphData

    private val _selectedGraph = MutableStateFlow(GraphType.VERTICAL)
    val selectedGraph: StateFlow<GraphType> = _selectedGraph

    init {
        generateGraphData()
    }

    fun selectGraph(graphType: GraphType) {
        _selectedGraph.value = graphType
    }

    private fun generateGraphData() {
        viewModelScope.launch {
            val data = listOf(
                // Simulando 35 datos con fechas variables
                RawGraphData(dateFromString("2024-05-30"), 1000),
                RawGraphData(dateFromString("2024-05-29"), 1000),
                RawGraphData(dateFromString("2024-05-28"), 1000),
                RawGraphData(dateFromString("2024-05-27"), 1000),
                RawGraphData(dateFromString("2024-05-26"), 1000),
                RawGraphData(dateFromString("2024-05-25"), 1000),
                RawGraphData(dateFromString("2024-05-24"), 1000),
                RawGraphData(dateFromString("2024-05-23"), 1000),
                RawGraphData(dateFromString("2024-05-22"), 1000),
                RawGraphData(dateFromString("2024-05-21"), 1000),
                RawGraphData(dateFromString("2024-05-20"), 1000),
                RawGraphData(dateFromString("2024-05-19"), 1000),
                RawGraphData(dateFromString("2024-05-18"), 1000),
                RawGraphData(dateFromString("2024-05-17"), 1000),
                RawGraphData(dateFromString("2024-05-16"), 1000),
                RawGraphData(dateFromString("2024-05-15"), 1000),
                RawGraphData(dateFromString("2024-05-14"), 1000),
                RawGraphData(dateFromString("2024-05-13"), 1000),
                RawGraphData(dateFromString("2024-05-12"), 1000),
                RawGraphData(dateFromString("2024-05-11"), 1000),
                RawGraphData(dateFromString("2024-05-10"), 1000),
                RawGraphData(dateFromString("2024-05-09"), 1000),
                RawGraphData(dateFromString("2024-05-08"), 1000),
                RawGraphData(dateFromString("2024-05-07"), 1000),
                RawGraphData(dateFromString("2024-05-06"), 1000),
                RawGraphData(dateFromString("2024-05-05"), 1000),
                RawGraphData(dateFromString("2024-05-04"), 1000),
                RawGraphData(dateFromString("2024-05-03"), 1000),
                RawGraphData(dateFromString("2024-05-02"), 1000),
                RawGraphData(dateFromString("2024-05-01"), 1000),
                RawGraphData(dateFromString("2024-04-30"), 1000),
                RawGraphData(dateFromString("2024-04-29"), 1000),
                RawGraphData(dateFromString("2024-04-28"), 1000),
                RawGraphData(dateFromString("2024-04-27"), 1000)
            )

            val groupedData = groupDataByCategory(data)
            _graphData.value = groupedData
        }
    }

    private fun dateFromString(dateString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.parse(dateString) ?: Date()
    }

    private fun groupDataByCategory(data: List<RawGraphData>): List<GraphData> {
        val now = Calendar.getInstance()

        val thisWeek = mutableListOf<RawGraphData>()
        val lastWeek = mutableListOf<RawGraphData>()
        val lastThirtyDays = mutableListOf<RawGraphData>()

        val startOfThisWeek = now.clone() as Calendar
        startOfThisWeek.set(Calendar.DAY_OF_WEEK, startOfThisWeek.firstDayOfWeek)

        val startOfLastWeek = startOfThisWeek.clone() as Calendar
        startOfLastWeek.add(Calendar.WEEK_OF_YEAR, -1)

        val startOfThirtyDays = now.clone() as Calendar
        startOfThirtyDays.add(Calendar.DAY_OF_YEAR, -30)

        data.forEach {
            val date = Calendar.getInstance()
            date.time = it.date

            when {
                date >= startOfThisWeek -> thisWeek.add(it)
                date >= startOfLastWeek && date < startOfThisWeek -> lastWeek.add(it)
                date >= startOfThirtyDays && date < startOfLastWeek -> lastThirtyDays.add(it)
            }
        }

        val thisWeekSum = thisWeek.sumOf { it.value }
        val lastWeekSum = lastWeek.sumOf { it.value }
        val lastThirtyDaysSum = lastThirtyDays.sumOf { it.value } + thisWeekSum + lastWeekSum

        return listOf(
            GraphData("Esta semana", thisWeekSum),
            GraphData("La semana pasada", lastWeekSum),
            GraphData("Últimos 30 días", lastThirtyDaysSum)
        )
    }
}

data class RawGraphData(val date: Date, val value: Int)

//------------------------------------------------
// Pantalla
val DarkBlue = Color(0xFF002366) // Color botón.
val LightBlueCard = Color(0xFFE3F2FD) // Celestito Cards
val LightBlueGradientStart = Color(0xFF42A5F5) // Principio gradiente celeste claro
val LightBlueGradientEnd = Color(0xFF1976D2) // Fin del gradiente celeste oscuro
val DarkBlueGradientStart = Color(0xFF1976D2) // Principio del gradiente azul claro
val DarkBlueGradientEnd = Color(0xFF42A5F5) // Fin del gradiente azul oscuro
val BlueFrance = Color(0xFF01579B) // Azul francia para el valor de los totales

// Funciones comunes a todas las cards
@Composable
fun ChartTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        LightBlueGradientStart,
                        LightBlueGradientEnd
                    )
                ),
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun ChartCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LightBlueCard)
    ) {
        Column {
            content()
        }
    }
}

// Gráficos
@Composable
fun BarChartH(data: List<GraphData>) {
    // Encontrar el valor máximo para escalar las barras
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
        // Usar la tarjeta de gráfico con título específico
        ChartCard {
            ChartTitle("Gráfico de barras horizontal")

            Column(modifier = Modifier.padding(16.dp)) {
                data.forEach { item ->
                    // Animar la barra según el valor relativo al máximo
                    val animatedWidth by animateFloatAsState(
                        targetValue = item.value.toFloat() / maxValue.toFloat(), label = ""
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        // Mostrar la categoría
                        Text(
                            text = item.category,
                            modifier = Modifier
                                .padding(2.dp)
                                .width(70.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(lineHeight = 18.sp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        // Dibujar la barra con el ancho animado
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .widthIn(max = 160.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            LightBlueGradientStart,
                                            LightBlueGradientEnd
                                        )
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .fillMaxWidth(fraction = animatedWidth)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Mostrar el valor
                        Text(
                            text = item.value.toString(),
                            modifier = Modifier
                                .width(60.dp)
                                .align(Alignment.CenterVertically),
                            color = DarkBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun BarChartV(data: List<GraphData>) {
    // Encontrar el valor máximo para escalar las barras
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
        // Usar la tarjeta de gráfico con título específico
        ChartCard {
            ChartTitle("Gráfico de barras vertical")

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(16.dp)
            ) {
                data.forEach { item ->
                    // Animar la barra según el valor relativo al máximo
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
                        // Mostrar el valor
                        Text(
                            text = item.value.toString(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            color = DarkBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Dibujar la barra con la altura animada
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .heightIn(max = 200.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            DarkBlueGradientStart,
                                            DarkBlueGradientEnd
                                        )
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .fillMaxHeight(fraction = animatedHeight)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Mostrar la categoría
                        Text(
                            text = item.category,
                            modifier = Modifier
                                .padding(2.dp)
                                .width(70.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(lineHeight = 18.sp)
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun PieChart(data: List<GraphData>) {
    // Calcular el total de los valores
    val total = data.sumOf { it.value.toDouble() }
    // Calcular el ángulo de barrido para cada segmento
    val sweepAngles = data.map { it.value.toFloat() / total.toFloat() * 360f }
    // Definir colores para los segmentos
    val colors = listOf(
        LightBlueGradientStart,
        LightBlueGradientEnd,
        BlueFrance
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        // Usar la tarjeta de gráfico con título específico
        ChartCard {
            ChartTitle("Gráfico de torta")

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var startAngle = 0f

                        // Dibujar arcos base en gris para efectos visuales
                        drawArc(
                            color = Color.Gray.copy(alpha = 0.3f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = true,
                            size = Size(size.width, size.height),
                            style = Stroke(width = 16f)
                        )
                        drawArc(
                            color = Color.Gray.copy(alpha = 0.2f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = true,
                            size = Size(size.width, size.height),
                            style = Stroke(width = 24f)
                        )
                        drawArc(
                            color = Color.Gray.copy(alpha = 0.1f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = true,
                            size = Size(size.width, size.height),
                            style = Stroke(width = 38f)
                        )
                        // Dibujar segmentos del gráfico de torta
                        data.forEachIndexed { index, _ ->
                            val sweepAngle = sweepAngles[index]
                            drawArc(
                                color = colors[index % colors.size],
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = true,
                                size = Size(size.width, size.height)
                            )
                            startAngle += sweepAngle
                        }

                        // Reiniciar el ángulo de inicio para etiquetas
                        startAngle = 0f
                        data.forEachIndexed { index, item ->
                            val sweepAngle = sweepAngles[index]
                            // Calcular la posición para las etiquetas
                            val angle = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                            val x = (size.width / 2 + (size.width / 4) * cos(angle)).toFloat()
                            val y = (size.height / 2 + (size.height / 4) * sin(angle)).toFloat()

                            // Dibujar las etiquetas
                            drawContext.canvas.nativeCanvas.apply {
                                drawText(
                                    item.value.toString(),
                                    x,
                                    y - 20,
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
                                    y + 25,
                                    android.graphics.Paint().apply {
                                        color = android.graphics.Color.WHITE
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        textSize = 30f
                                        isAntiAlias = true
                                    }
                                )
                            }
                            startAngle += sweepAngle
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun GraphScreen(viewModel: GraphViewModel = viewModel()) {
    val data by viewModel.graphData.collectAsState()
    val selectedGraph by viewModel.selectedGraph.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // esto es para que la pantalla tenga scroll
        horizontalAlignment = Alignment.CenterHorizontally // Centrar el contenido horizontalmente
    ) {
        // Dropdown para seleccionar el tipo de gráfico
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center) // Centrar el contenido del Box
        ) {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueFrance,
                    contentColor = Color.White
                ),
                border = BorderStroke(3.dp, DarkBlueGradientEnd)
            ) {
                Text(text = "Seleccionar gráfico: ${selectedGraph.name}")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Gráfico de barras horizontal") },
                    onClick = {
                        viewModel.selectGraph(GraphType.HORIZONTAL)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Gráfico de barras vertical") },
                    onClick = {
                        viewModel.selectGraph(GraphType.VERTICAL)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Gráfico de torta") },
                    onClick = {
                        viewModel.selectGraph(GraphType.TORTA)
                        expanded = false
                    }
                )
            }
        }

        // Mostrar el gráfico seleccionado
        Spacer(modifier = Modifier.height(16.dp))
        when (selectedGraph) {
            GraphType.HORIZONTAL -> BarChartH(data = data)
            GraphType.VERTICAL -> BarChartV(data = data)
            GraphType.TORTA -> PieChart(data = data)
        }
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