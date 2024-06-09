package com.example.tirateunpaso.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// View model para gestionar el estado y los datos del calendario
class CalendarViewModel : ViewModel() {
    private val calendar = Calendar.getInstance()
    private val localeSpanish = Locale("es", "ES") // Instancia de Locale para español

    private val _selectedMonth = mutableIntStateOf(calendar.get(Calendar.MONTH))
    val selectedMonth: State<Int> = _selectedMonth

    private val _selectedYear = mutableIntStateOf(calendar.get(Calendar.YEAR))
    val selectedYear: State<Int> = _selectedYear

    private val _graphDataCalendar = MutableStateFlow<List<RawData>>(emptyList())
    val graphDataCalendar: StateFlow<List<RawData>> = _graphDataCalendar

    init {
        generateGraphData()
    }

    // Función para cambiar el mes y el año seleccionados
    fun changeMonthYear(month: Int, year: Int) {
        _selectedMonth.intValue = month
        _selectedYear.intValue = year
        generateGraphData() // Regenerar datos cuando cambie el mes/año
    }

    // Función para obtener el nombre del mes actual en español
    fun getCurrentMonthName(): String {
        calendar.apply {
            set(Calendar.MONTH, _selectedMonth.intValue)
            set(Calendar.YEAR, _selectedYear.intValue)
        }
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, localeSpanish) ?: ""
    }

    // Función para obtener todos los días del mes seleccionado
    fun getDaysOfMonth(): List<Int> {
        calendar.apply {
            set(Calendar.YEAR, _selectedYear.intValue)
            set(Calendar.MONTH, _selectedMonth.intValue)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        return (1..daysInMonth).toList()
    }

    // Función para obtener el primer día del mes
    fun getFirstDayOfMonth(): Int {
        calendar.apply {
            set(Calendar.YEAR, _selectedYear.intValue)
            set(Calendar.MONTH, _selectedMonth.intValue)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    // Función para obtener la fecha de hoy si está en el mes y año seleccionados
    fun getToday(): Int {
        val todayCalendar = Calendar.getInstance()
        if (todayCalendar.get(Calendar.YEAR) == _selectedYear.intValue &&
            todayCalendar.get(Calendar.MONTH) == _selectedMonth.intValue) {
            return todayCalendar.get(Calendar.DAY_OF_MONTH)
        }
        return -1 // Indicar que hoy no está en el mes/año seleccionado
    }

    // Función para generar datos para el gráfico (saca del DataGenerator)
    private fun generateGraphData() {
        val rawGraphData = DataGenerator.generateRawGraphData()
        _graphDataCalendar.value = rawGraphData
    }
}

//-----------------------------------------------------
// Función Composable para la pantalla del calendario
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = remember { CalendarViewModel() }) {
    val monthNavigation: (Int) -> Unit = { increment ->
        val currentMonth = viewModel.selectedMonth.value
        val currentYear = viewModel.selectedYear.value

        val newMonth = (currentMonth + increment + 12) % 12
        val newYear = if (newMonth == 0 && increment < 0) currentYear - 1
        else if (newMonth == 11 && increment > 0) currentYear + 1
        else currentYear

        viewModel.changeMonthYear(newMonth, newYear)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Tarjeta para el calendario
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = LightBlueGradientEnd,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlueCard.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 16.dp, start = 6.dp, end = 6.dp)
            ) {
                // Encabezado con el nombre del mes y botones de navegación
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { monthNavigation(-1) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BlueFrance)
                                .border(2.dp, LightBlueGradientStart, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Mes anterior",
                                tint = Color.White
                            )
                        }
                    }

                    Text(
                        text = "${viewModel.getCurrentMonthName()} ${viewModel.selectedYear.value}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = BlueFrance,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = { monthNavigation(1) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BlueFrance)
                                .border(2.dp, LightBlueGradientStart, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Próximo mes",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Encabezados de los días de la semana
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB").forEach { day ->
                        Box(
                            modifier = Modifier.weight(1f).padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = BlueFrance,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Mostrar los días del mes en formato de calendario
                val daysOfMonth = viewModel.getDaysOfMonth()
                val firstDayOfMonth = viewModel.getFirstDayOfMonth()
                val daysInWeek = 7
                val today = viewModel.getToday()

                // Lista de días rellenada con nulos para alinearse con el primer día correcto
                val paddedDays = List(firstDayOfMonth - 1) { null } + daysOfMonth

                // Asegurar que la última fila tenga exactamente 7 elementos
                val totalCells = if (paddedDays.size % daysInWeek == 0) paddedDays.size
                else paddedDays.size + (daysInWeek - paddedDays.size % daysInWeek)
                val completeDays = paddedDays + List(totalCells - paddedDays.size) { null }

                val chunkedDays = completeDays.chunked(daysInWeek)

                chunkedDays.forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        week.forEach { day ->
                            if (day != null) {
                                val calendar = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, viewModel.selectedYear.value)
                                    set(Calendar.MONTH, viewModel.selectedMonth.value)
                                    set(Calendar.DAY_OF_MONTH, day)
                                    set(Calendar.HOUR_OF_DAY, 0)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }

                                // Obtener el valor del día desde viewModel.graphDataCalendar
                                val value = viewModel.graphDataCalendar.value.firstOrNull {
                                    val graphCalendar = Calendar.getInstance().apply {
                                        time = it.date
                                        set(Calendar.HOUR_OF_DAY, 0)
                                        set(Calendar.MINUTE, 0)
                                        set(Calendar.SECOND, 0)
                                        set(Calendar.MILLISECOND, 0)
                                    }
                                    graphCalendar.time == calendar.time
                                }?.value

                                // Determinar el color de fondo y el color del texto según el valor
                                val (backgroundColor, textColor) = if (value != null && value != 0) {
                                    DarkBlue to Color.White
                                } else {
                                    Color.LightGray to Color.Black
                                }

                                // Determinar el color del borde y grosor para el día actual
                                val borderColor = if (day == today) LightBlueGradientStart else Color.Transparent
                                val borderWidth = if (day == today) 4.dp else 2.dp

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                        .aspectRatio(1f) // Asegurar que el Box sea cuadrado
                                        .background(backgroundColor)
                                        .border(borderWidth, borderColor, RoundedCornerShape(0.dp))
                                        .clickable { /* Acción al hacer clic en el día, ver qué hacemos */ },
                                    contentAlignment = Alignment.Center
                                )
                                {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = day.toString(),
                                            style = TextStyle(color = textColor)
                                        )
                                        if (value != null && value != 0) {
                                            Text(
                                                text = value.toString(),
                                                style = TextStyle(fontSize = 10.sp, color = textColor)
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.height(12.dp))
                                        }
                                    }
                                }
                            } else {
                                // Añadir espacio vacío para los días nulos
                                Spacer(modifier = Modifier.weight(1f).padding(4.dp).aspectRatio(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}