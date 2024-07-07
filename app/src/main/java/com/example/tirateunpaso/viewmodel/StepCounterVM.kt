package com.example.tirateunpaso.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StepCounterVM: ViewModel() {
    private val _stepCount = MutableStateFlow(StepCounterUiState())
    val uiState: StateFlow<StepCounterUiState> = _stepCount.asStateFlow()

    fun UpdateSteps(steps: Long) = viewModelScope.launch{
        _stepCount.value = StepCounterUiState(steps)
    }
}

data class StepCounterUiState(val stepCount:Long = 0)