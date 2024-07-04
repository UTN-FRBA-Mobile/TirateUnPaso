package com.example.tirateunpaso.ui.screens

import TirateUnPasoTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tirateunpaso.ui.components.ExpandableCard
import com.example.tirateunpaso.ui.values
import com.example.tirateunpaso.ui.viewmodels.AchievementViewModel
import com.example.tirateunpaso.ui.viewmodels.AppViewModelProvider
import com.example.tirateunpaso.ui.viewmodels.asListContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AchievementsScreen(
    onHomeClick: () -> Unit,
    viewModel: AchievementViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(key1 = viewModel) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.retrieveAchievements()
        }
    }

    TirateUnPasoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values.defaultPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onHomeClick,
                    modifier = Modifier.align(alignment = Alignment.Start),
                ) {
                    Text(
                        text = "Volver a home",
                        fontSize = values.fontsize
                    )
                }
                Spacer(modifier = Modifier.height(values.defaultSpacing))

                var expandedItem by remember { mutableIntStateOf(-1) }

                LazyColumn {
                    items(viewModel.achievementsUiState.achievements.asListContent()) { content ->
                        ExpandableCard(
                            content = content,
                            expanded = expandedItem == content.id,
                            onClickExpanded = { id ->
                                expandedItem = if (expandedItem == id) {
                                    -1
                                } else {
                                    id
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

