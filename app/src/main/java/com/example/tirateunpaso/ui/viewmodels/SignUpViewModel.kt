package com.example.tirateunpaso.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tirateunpaso.database.achievement.Achievement
import com.example.tirateunpaso.database.achievement.AchievementsRepository
import com.example.tirateunpaso.database.user.User
import com.example.tirateunpaso.database.user.UsersRepository
import com.example.tirateunpaso.ui.components.Content

class SignUpViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    var signUpUiState by mutableStateOf(SignUpUiState())
        private set

    suspend fun signUp() {
        usersRepository.insertOne(
            User(username = "Juan", hashedPassword = "pepito",
                 age = 25, height = 170)
        )
    }

    fun setUsername(username: String) {
        signUpUiState = signUpUiState.copy(username = username)
    }

    fun setEmail(email: String) {
        signUpUiState = signUpUiState.copy(email = email)
    }

    fun setPassword(password: String) {
        signUpUiState = signUpUiState.copy(password = password)
    }

    fun setSecondPassword(secondPassword: String) {
        signUpUiState = signUpUiState.copy(secondPassword = secondPassword)
    }

    fun setAge(age: String) {
        signUpUiState = signUpUiState.copy(age = age)
    }

    fun setHeight(height: String) {
        signUpUiState = signUpUiState.copy(height = height)
    }

    fun setSex(index: Int) {
        var finalIndex = index
        if (index == 0){
            finalIndex = 3
        }
        signUpUiState = signUpUiState.copy(sex = signUpUiState.sexList[finalIndex])
    }

    fun toggleIsExpanded() {
        signUpUiState = signUpUiState.copy(isExpanded = !signUpUiState.isExpanded)
    }

    fun setIsExpanded(isExpanded: Boolean) {
        signUpUiState = signUpUiState.copy(isExpanded = isExpanded)
    }

    fun verifyMatchingPasswords(onSignUpClick: () -> Unit) {
        val matchingPasswords = signUpUiState.password == signUpUiState.secondPassword

        if(matchingPasswords) {
            onSignUpClick()
        }

        signUpUiState = signUpUiState.copy(matchingPasswords = matchingPasswords)
    }

    fun fieldsCompleted(): Boolean {
        return signUpUiState.fieldsAreCompleted()
    }
}

/**
 * UI state for AchievementsScreen
 */
data class SignUpUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val secondPassword: String = "",
    val age: String = "",
    val height: String = "",
    val sexList: List<String> = listOf("Selecciona tu sexo","Masculino", "Femenino", "Otro"),
    val sex: String = sexList[0],
    val isExpanded: Boolean = false,
    val matchingPasswords: Boolean = false
)

fun SignUpUiState.fieldsAreCompleted(): Boolean =
    username.isNotEmpty() &&
            email.isNotEmpty() &&
            password.isNotEmpty() &&
            secondPassword.isNotEmpty() &&
            age.isNotEmpty() &&
            height.isNotEmpty() &&
            sex != sexList[0]
