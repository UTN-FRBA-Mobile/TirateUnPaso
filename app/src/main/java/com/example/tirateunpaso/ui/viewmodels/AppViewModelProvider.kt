package com.example.tirateunpaso.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tirateunpaso.TirateUnPasoApplication
import com.example.tirateunpaso.database.achievement.Achievement
import com.example.tirateunpaso.database.achievement.AchievementDao
import com.example.tirateunpaso.database.achievement.AchievementsRepository
import kotlinx.coroutines.launch

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AchievementEditViewModel
        initializer {
            AchievementViewModel(
                inventoryApplication().container.achievementsRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): TirateUnPasoApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TirateUnPasoApplication)