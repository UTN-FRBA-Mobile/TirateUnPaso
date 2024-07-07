package com.example.tirateunpaso.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tirateunpaso.database.healthadvice.HealthAdvice
import com.example.tirateunpaso.database.healthadvice.HealthAdvicesRepository
import kotlinx.coroutines.flow.firstOrNull

class HealthAdviceViewModel(
    private val healthAdvicesRepository: HealthAdvicesRepository
) : ViewModel() {

    var healthAdvicesUiState by mutableStateOf(HealthAdvicesUiState(null))
        private set

    suspend fun retrieveRandomHealthAdvice() {
        val healthAdvice = healthAdvicesRepository.getOneStream(((0..19).random())).firstOrNull()
        healthAdvicesUiState = HealthAdvicesUiState(healthAdvice)
    }
}

/**
 * UI state for HealthAdvicesScreen
 */
data class HealthAdvicesUiState(
    val healthAdvice: HealthAdvice?
)