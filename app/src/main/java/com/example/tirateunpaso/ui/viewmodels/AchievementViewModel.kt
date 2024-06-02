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

        achievementsRepository.deleteAll()

        achievementsRepository.insertMany(
            Achievement(0,"Caminar 1000 pasos", 1000, 1000, true),
            Achievement(1,"Caminar 2000 pasos", 1000, 2000, false),
            Achievement(2,"Caminar 300 kilómetros", 251, 300, false),
            Achievement(3,"Quemar 700 calorías",  445, 700, false),
            Achievement(4,"Invitar a 5 amigos", 0, 5, false),
            Achievement(5,"Usar la app durante 30 días consecutivos", 18, 30, false)
        )

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
        Content(a.id, a.title, a.unlocked, "%d / %d".format(a.actualScore, a.requiredScore))
    }
