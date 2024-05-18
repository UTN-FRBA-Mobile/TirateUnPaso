package com.example.tirateunpaso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tirateunpaso.ui.components.Content
import com.example.tirateunpaso.ui.components.ExpandableCard
import com.example.tirateunpaso.ui.values

val contentList = listOf(
    Content(0,"Caminar 1000 pasos", true, "1000 / 1000"),
    Content(1,"Caminar 300 kilómetros", false, "251 / 300"),
    Content(2,"Quemar 700 calorías", false, "445 / 700"),
    Content(3,"Invitar a 5 amigos", false, "0 / 5"),
    Content(4,"Usar la app durante 30 días consecutivos", false, "18 / 30")
)
@Composable
fun AchievementsScreen(
    onHomeClick:() -> Unit,
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
        ){
            Text(
                text = "Volver a home",
                fontSize = values.fontsize
            )
        }
        Spacer(modifier = Modifier.height(values.defaultSpacing))

        var expandedItem by remember {
            mutableStateOf(-1)
        }
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn {
                items(contentList){
                        content ->
                    ExpandableCard(
                        content = content,
                        expanded = expandedItem == content.id,
                        onClickExpanded = {
                                id ->
                            expandedItem = if(expandedItem == id){
                                -1
                            }else{
                                id
                            }
                        }
                    )
                }
            }
        }
    }
}

