package com.example.tirateunpaso.ui.screens

import TirateUnPasoTheme
import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tirateunpaso.DataGenerator
import com.example.tirateunpaso.R
import com.example.tirateunpaso.RawData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

data class GraphData(val category: String, val value: Int)

class GraphViewModel : ViewModel() {
    private val _graphData = MutableStateFlow<List<GraphData>>(emptyList())
    val graphData: StateFlow<List<GraphData>> = _graphData

    private val _selectedGraph = MutableStateFlow(GraphType.WEEKLY_HORIZONTAL)
    val selectedGraph: StateFlow<GraphType> = _selectedGraph

    private val _stepsToday = MutableStateFlow(0)
    val stepsToday: StateFlow<Int> = _stepsToday

    private val _lastSevenDaysData = MutableStateFlow<List<GraphData>>(emptyList())
    val lastSevenDaysData: StateFlow<List<GraphData>> = _lastSevenDaysData

    init {
        generateGraphData()
    }

    fun selectGraph(graphType: GraphType) {
        _selectedGraph.value = graphType
    }

    private fun generateGraphData() {
        viewModelScope.launch {
            val rawGraphData = DataGenerator.generateRawGraphData()

            val stepsTodayValue = calculateStepsTodayFromData(rawGraphData)
            _stepsToday.value = stepsTodayValue

            val lastSevenDaysData = getLastSevenDaysData(rawGraphData)
            _lastSevenDaysData.value = lastSevenDaysData

            val groupedData = groupDataByCategory(rawGraphData)
            _graphData.value = groupedData
        }
    }

    private fun groupDataByCategory(data: List<RawData>): List<GraphData> {
        // Implementación para agrupar datos por categoría
        val now = Calendar.getInstance()
        now.set(Calendar.HOUR_OF_DAY, 0)
        now.set(Calendar.MINUTE, 0)
        now.set(Calendar.SECOND, 0)
        now.set(Calendar.MILLISECOND, 0)

        // Define el rango de fechas para esta semana y la semana pasada
        val endOfThisWeek = now.clone() as Calendar
        val startOfThisWeek = now.clone() as Calendar
        startOfThisWeek.add(Calendar.DAY_OF_YEAR, -6)

        val endOfLastWeek = startOfThisWeek.clone() as Calendar
        val startOfLastWeek = endOfLastWeek.clone() as Calendar
        startOfLastWeek.add(Calendar.DAY_OF_YEAR, -7) // Inicia 14 días antes del inicio de esta semana
        endOfLastWeek.add(Calendar.DAY_OF_YEAR, -1) // Termina 7 días antes del inicio de esta semana

        // Clasificar los datos en las categorías correspondientes
        val thisWeek = data.filter { it.date in startOfThisWeek.time..endOfThisWeek.time }
        val lastWeek = data.filter { it.date in startOfLastWeek.time..endOfLastWeek.time }
        val lastThirtyDays = data.take(30)

        // Calcular las sumas para cada categoría
        val thisWeekSum = thisWeek.sumOf { it.value }
        val lastWeekSum = lastWeek.sumOf { it.value }
        val lastThirtyDaysSum = lastThirtyDays.sumOf { it.value }

        // Devolver los datos agrupados
        return listOf(
            GraphData("Esta\nsemana", thisWeekSum),
            GraphData("La semana\npasada", lastWeekSum),
            GraphData("TOTAL\n(30 días)", lastThirtyDaysSum)
        )
    }

    private fun calculateStepsTodayFromData(data: List<RawData>): Int {
        // Implementación para calcular pasos de hoy desde los datos
        val now = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayString = dateFormat.format(now.time)

        val today = data.find { dateFormat.format(it.date) == todayString }
        return today?.value ?: 0
    }

    private fun getLastSevenDaysData(data: List<RawData>): List<GraphData> {
        // Implementación para obtener datos de los últimos 7 días
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val lastSevenDaysData = mutableListOf<GraphData>()

        // Crear un mapa para almacenar los valores de los últimos 7 días
        val stepsMap = mutableMapOf<String, Int>()

        // Obtener los últimos 7 días
        for (i in 0..6) {
            val dateString = dateFormat.format(calendar.time)
            stepsMap[dateString] = 0
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        // Llenar el mapa con los datos
        data.forEach {
            val dateString = dateFormat.format(it.date)
            if (stepsMap.containsKey(dateString)) {
                stepsMap[dateString] = it.value
            }
        }

        // Convertir el mapa en una lista ordenada por fecha descendente
        val sortedList = stepsMap.toList().sortedByDescending { dateFormat.parse(it.first) }

        // Procesar la lista ordenada y agregar a lastSevenDaysData
        sortedList.forEachIndexed { index, entry ->
            val dayString = if (index == 0) "HOY" else {
                val date = LocalDate.parse(entry.first)
                when (date.dayOfWeek) {
                    DayOfWeek.MONDAY -> "LUN"
                    DayOfWeek.TUESDAY -> "MAR"
                    DayOfWeek.WEDNESDAY -> "MIÉ"
                    DayOfWeek.THURSDAY -> "JUE"
                    DayOfWeek.FRIDAY -> "VIE"
                    DayOfWeek.SATURDAY -> "SÁB"
                    DayOfWeek.SUNDAY -> "DOM"
                    else -> ""
                }
            }
            lastSevenDaysData.add(GraphData(dayString, entry.second))
        }

        return lastSevenDaysData
    }
}

// -----------------------------------------------------------------------
// Pantallas

// CARD DE PASOS DE HOY
@Composable
fun StepsCard(stepsToday: Int) {
    TirateUnPasoTheme { // Envolver en el tema principal de la aplicación
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            ChartCard {
                // ChartTitle("Pasos de hoy")

                Box( // box de la imagen
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.75f))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_running),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight, // FillHeight para no cortar contenido
                        alignment = Alignment.TopCenter, // imagen en la parte superior
                        modifier = Modifier.fillMaxSize()
                    )
                    Box( // Box que contiene el texto blanco con fondo gris
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(x = (-4).dp)
                            .offset(y = (-12).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 1f), shape = RoundedCornerShape(8.dp))
                                .padding(vertical = 4.dp, horizontal = 2.dp)
                                .clickable(onClick = { /* No hacer nada */ }), // Esto es por si después queremos agregar algo
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "PASOS HOY",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    ),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "$stepsToday",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// TÍTULO PARA CARD
@Composable
fun ChartTitle(title: String) {
    TirateUnPasoTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.primary
                        )
                    ),
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

// CARD CELESTE
@Composable
fun ChartCard(content: @Composable () -> Unit) {
    TirateUnPasoTheme {
        Card(
            modifier = Modifier.fillMaxWidth()
                /*
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp))

                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                ) */,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier) {
                content()
            }
        }
    }
}

// ----------------------------------------------------------------
// GRÁFICOS
@Composable
fun BarChartH(title: String, data: List<GraphData>) {
    // Encontrar el valor máximo para escalar las barras
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    TirateUnPasoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // Usa las funciones de card y title.
            ChartCard {
                ChartTitle(title)

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
                                fontSize = 11.sp,
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
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
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
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BarChartV(title: String, data: List<GraphData>) {
    // Encontrar el valor máximo para escalar las barras
    val maxValue = data.maxOfOrNull { it.value } ?: 1

    TirateUnPasoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // Usa las funciones de card y title.
            ChartCard {
                ChartTitle(title)

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
                                .weight(1f) // Ajustar el ancho de cada barra proporcionalmente
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
                                color = MaterialTheme.colorScheme.onSecondary,
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
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
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
                                fontSize = 11.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(lineHeight = 18.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PieChart(title: String, data: List<GraphData>) {
    // Calcular el total de los valores
    val total = data.sumOf { it.value.toDouble() }
    // Calcular el ángulo de barrido para cada segmento
    val sweepAngles = data.map { it.value.toFloat() / total.toFloat() * 360f }
    // Definir colores para los segmentos
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    )

    TirateUnPasoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // Usa las funciones de card y title.
            ChartCard {
                ChartTitle(title)

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
                                        Paint().apply {
                                            color = android.graphics.Color.WHITE
                                            textAlign = Paint.Align.CENTER
                                            textSize = 40f
                                            isAntiAlias = true
                                        }
                                    )
                                    drawText(
                                        item.category,
                                        x,
                                        y + 25,
                                        Paint().apply {
                                            color = android.graphics.Color.WHITE
                                            textAlign = Paint.Align.CENTER
                                            textSize = 30f
                                            isAntiAlias = true
                                        }
                                    )
                                }
                                startAngle += sweepAngle
                            }
                        }
                    }
                }
            }
        }
    }
}

//------------------------------------------------------------------
// SELECTOR DE GRÁFICOS
// Enum para hacer la selección
enum class GraphType {
    WEEKLY_HORIZONTAL,
    WEEKLY_VERTICAL,
    HORIZONTAL,
    VERTICAL,
    TORTA
}

// Composable para el botón con el menú desplegable
@Composable
fun ButtonDropdown(onGraphSelected: (GraphType) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    TirateUnPasoTheme { // Envolver en el tema principal de la aplicación
        Box(modifier = modifier) {
            Box(
                modifier = Modifier
                    .size(32.dp) // Tamaño del botón
                    .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary, CircleShape)
                    .clickable(onClick = { expanded = true })
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down), // Icono de flecha hacia abajo
                    contentDescription = "Dropdown Arrow",
                    tint = Color.White,
                    modifier = Modifier
                        .size(26.dp) // Tamaño del icono
                        .align(Alignment.Center)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Semanal - Barras horizontal") },
                    onClick = {
                        onGraphSelected(GraphType.WEEKLY_HORIZONTAL)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Semanal - Barras vertical") },
                    onClick = {
                        onGraphSelected(GraphType.WEEKLY_VERTICAL)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Resumen - Barras horizontal") },
                    onClick = {
                        onGraphSelected(GraphType.HORIZONTAL)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Resumen - Barras vertical") },
                    onClick = {
                        onGraphSelected(GraphType.VERTICAL)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Resumen - Torta") },
                    onClick = {
                        onGraphSelected(GraphType.TORTA)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Composable para el selector de gráficos
@Composable
fun GraphContainer(
    selectedGraph: GraphType,
    data: List<GraphData>,
    lastSevenDaysData: List<GraphData>,
    onGraphSelected: (GraphType) -> Unit
) {
    TirateUnPasoTheme { // Envolver en el tema principal de la aplicación
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Mostrar el gráfico seleccionado
            Column(modifier = Modifier.align(Alignment.Center)) {
                when (selectedGraph) {
                    GraphType.WEEKLY_HORIZONTAL -> BarChartH(
                        "Semanal - Barras horizontal",
                        data = lastSevenDaysData
                    )
                    GraphType.WEEKLY_VERTICAL -> BarChartV(
                        "Semanal - Barras vertical",
                        data = lastSevenDaysData
                    )
                    GraphType.HORIZONTAL -> BarChartH("Resumen - Barras horizontal", data = data)
                    GraphType.VERTICAL -> BarChartV("Resumen - Barras vertical", data = data)
                    GraphType.TORTA -> PieChart("Resumen - Torta", data = data)
                }
            }

            // Usa el botón con dropdown en la esquina superior derecha del selector de gráficos
            ButtonDropdown(
                onGraphSelected = onGraphSelected,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = 6.dp) // Ajuste de posición del botón
            )
        }
    }
}
