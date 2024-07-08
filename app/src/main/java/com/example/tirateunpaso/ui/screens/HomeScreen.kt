package com.example.tirateunpaso.ui.screens

import TirateUnPasoTheme
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
// import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tirateunpaso.database.healthadvice.HealthAdvice
import com.example.tirateunpaso.ui.components.BottomButton
import com.example.tirateunpaso.ui.components.HeaderText
import com.example.tirateunpaso.ui.values
import com.example.tirateunpaso.ui.viewmodels.AppViewModelProvider
import com.example.tirateunpaso.ui.viewmodels.HealthAdviceViewModel
import kotlinx.coroutines.launch

/*
val recommendations = listOf(
    "Caminá al menos 30 minutos todos los días.",
    "Intentá usar las escaleras en lugar del ascensor siempre que puedas.",
    "No te olvides de elongar después de cada sesión de ejercicio.",
    "Mantené una buena hidratación a lo largo del día.",
    "Calentá correctamente antes de empezar a correr para evitar lesiones.",
    "Usá calzado cómodo y adecuado para caminar.",
    "Programá caminatas al aire libre para disfrutar del entorno.",
    "Registrá tus pasos diarios para mantener un seguimiento de tu progreso.",
    "Buscá compañía para caminar y hacerlo más entretenido.",
    "Variá tu ruta de caminata para evitar el aburrimiento y estimular diferentes músculos.",
    "Aprovechá los momentos de descanso para estirar y moverte.",
    "Incorporá ejercicios de fuerza como sentadillas o flexiones para complementar tus caminatas.",
    "Hacé pausas activas durante el día para estirar y mover las piernas.",
    "Usá está app para establecer metas y recibir recordatorios de actividad física.",
    "Escuchá música o podcasts motivadores durante tus caminatas para mantenerte inspirado."
)

@Composable
fun RecommendationCard(recommendations: List<String>) {
    val random = remember { java.util.Random() }
    val randomIndex = remember { random.nextInt(recommendations.size) }
    val recommendation = recommendations[randomIndex]

    ChartCard {
        ChartTitle("Recomendación")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = values.defaultSpacing)
        ) {
            Text(
                text = recommendation,
                modifier = Modifier
                    .padding(2.dp),
                // .width(70.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = TextStyle(lineHeight = 18.sp)
            )
        }
    }
}
*/

@Composable
fun RecommendationCard(healthAdvice: HealthAdvice?) {
    ChartCard {
        ChartTitle("Recomendación")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = values.defaultSpacing)
        ) {
            Text(
                text = healthAdvice?.category ?: "Pedime un consejo!",
                modifier = Modifier
                    .padding(2.dp).fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = TextStyle(lineHeight = 18.sp)
            )
            Text(
                text = healthAdvice?.description ?: "",
                modifier = Modifier
                    .padding(4.dp).fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = TextStyle(lineHeight = 18.sp)
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    onShowHealthAdviceClick: (HealthAdvice?) -> Unit,
    onStatisticsClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    viewModel: GraphViewModel = viewModel(),
    healthAdviceViewModel: HealthAdviceViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val scope = rememberCoroutineScope()

    TirateUnPasoTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(values.defaultPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderText(
                text = "Mi actividad hoy",
                modifier = Modifier.padding(vertical = values.defaultPadding),
            )

            StepsCard(stepsToday = viewModel.stepsToday.value)

            Spacer(modifier = Modifier.height(values.defaultSpacing))

            RecommendationCard(healthAdvice = healthAdviceViewModel.healthAdvicesUiState.healthAdvice)

            Spacer(modifier = Modifier.height(values.defaultSpacing))

            Button(
                onClick = {
                    scope.launch {
                        healthAdviceViewModel.retrieveRandomHealthAdvice()
                        onShowHealthAdviceClick(healthAdviceViewModel.healthAdvicesUiState.healthAdvice)
                    }
                },
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            ) {
                Text(
                    text = if (healthAdviceViewModel.healthAdvicesUiState.healthAdvice == null) "Dame un consejo!" else "Dame otro consejo!",
                    fontSize = values.fontsize,
                    color = Color.White
                )
            }
            /*
        Spacer(modifier = Modifier.height(values.defaultSpacing))
        CircularButton(
            onClick = onStatisticsClick,
            number = 1000,
            measure = "pasos dados"
        )
        Spacer(modifier = Modifier.height(values.defaultSpacing))
        CircularButton(
            onClick = onStatisticsClick,
            number = 1000,
            measure = "km recorridos"
        )
        */
        }
        BottomButton(
            alignment = Alignment.BottomStart,
            onClick = onAchievementsClick,
            text = "Logros"
        )

        BottomButton(
            alignment = Alignment.BottomEnd,
            onClick = onStatisticsClick,
            text = "Estadísticas"
        )
    }
}


@Preview(showSystemUi = true)
@Composable
fun DefaultHomePreview() {
    HomeScreen({},{},{})
}