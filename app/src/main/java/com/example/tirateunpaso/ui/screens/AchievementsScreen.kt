package com.example.tirateunpaso.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.tirateunpaso.ui.components.Content
import com.example.tirateunpaso.ui.components.ExpandableCard

const val LOREM_TEXT = "sample"

val contentList = listOf(
    Content(0,"Titulo 1", LOREM_TEXT),
    Content(1,"Titulo 2", LOREM_TEXT),
    Content(2,"Titulo 3", LOREM_TEXT),
    Content(3,"Titulo 4", LOREM_TEXT),
    Content(4,"Titulo 5", LOREM_TEXT)
)
@Composable
fun AchievementsScreen(
    onHomeClick:() -> Unit,
) {
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