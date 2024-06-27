package com.example.tirateunpaso.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tirateunpaso.database.achievement.Achievement
import com.example.tirateunpaso.database.achievement.AchievementsRepository
import com.example.tirateunpaso.ui.components.Content

class AchievementViewModel(
    private val achievementsRepository: AchievementsRepository
) : ViewModel() {

    var achievementsUiState by mutableStateOf(AchievementsUiState())
        private set

    suspend fun retrieveAchievements() {
        achievementsUiState = AchievementsUiState(achievementsRepository.getAllList())
    }
}

/**
 * UI state for AchievementsScreen
 */
data class AchievementsUiState(
    val achievements: List<Achievement> = emptyList()
)

fun List<Achievement>.asListContent(): List<Content> =
    this.map { a: Achievement ->
        Content(a.id, a.title, a.actualScore >= a.requiredScore, "%d / %d".format(a.actualScore, a.requiredScore))
    }
