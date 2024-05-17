package com.example.tirateunpaso.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tirateunpaso.ui.values

@Composable
fun BottomButton(
    alignment : Alignment,
    onClick: () -> Unit,
    text: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .wrapContentSize(align = alignment)
            .padding(values.defaultPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(width = 146.dp, height = 40.dp)
        ){
            Text(
                text = text,
                fontSize = values.fontsize
            )
        }
    }
}