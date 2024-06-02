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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.SpanStyle
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

import java.text.SimpleDateFormat
import java.util.*


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

            //esto es para agrupar, arma lista de fechas con su valor
            when {
                date >= startOfThisWeek -> thisWeek.add(it)
                date >= startOfLastWeek && date < startOfThisWeek -> lastWeek.add(it)
                date >= startOfThirtyDays && date < startOfLastWeek -> lastThirtyDays.add(it)
            }
        }

        // Esto es para sumar
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
                                .padding(vertical = 10.dp) // Separación entre filas (barras)
                        ) {
                            Text(
                                text = item.category,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .width(70.dp), // esto hace que el texto no ocupe toda una línea.
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 2, // quiero maximo 2 lineas.
                                overflow = TextOverflow.Ellipsis, // por las dudas
                                style = TextStyle(
                                    lineHeight = 18.sp // tamaño de línea (cuando separa la cadena en 2 líneas)
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .height(20.dp)
                                    .widthIn(max = 160.dp) // Ancho máximo para las barras
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF42A5F5),
                                                Color(0xFF1976D2)
                                            )
                                        ),
                                        shape = RoundedCornerShape(4.dp)  // Barras redondeadas
                                    )
                                    .fillMaxWidth(fraction = animatedWidth)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.value.toString(),
                                modifier = Modifier
                                    .width(60.dp)
                                    .align(Alignment.CenterVertically),
                                color = Color(0xFF002366), // Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center  // Alineación centrada para los valores
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
                                color = Color(0xFF002366), // Color azul del valor
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
                                        shape = RoundedCornerShape(4.dp)  // Barras redondeadas
                                    )
                                    .fillMaxHeight(fraction = animatedHeight)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.category,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .width(70.dp), // esto hace que el texto no ocupe toda una línea.
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 2, // quiero maximo 2 lineas.
                                overflow = TextOverflow.Ellipsis, // por las dudas
                                style = TextStyle(
                                    lineHeight = 18.sp // tamaño de línea (cuando separa la cadena en 2 líneas)
                                )
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
        Color(0xFF42A5F5),
        Color(0xFF1976D2),
        Color(0xFF01579B)
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

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            var startAngle = 0f

                            // Dibujar las sombras...
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

                            // Dibujar el gráfico de torta
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

                            // Dibujar las etiquetas después del gráfico
                            startAngle = 0f
                            data.forEachIndexed { index, item ->
                                val sweepAngle = sweepAngles[index]
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
                                Box( // color de leyenda
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(colors[index % colors.size])
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = Color.Gray)) {
                                            append("${item.category}: ")
                                        }
                                        withStyle(
                                            style = SpanStyle(
                                                color = Color(0xFF002366),
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append("${item.value}")
                                        }
                                    },
                                    modifier = Modifier.padding(2.dp),
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    style = TextStyle(
                                        lineHeight = 18.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
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