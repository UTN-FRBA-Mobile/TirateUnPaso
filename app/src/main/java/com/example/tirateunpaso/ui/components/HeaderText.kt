package com.example.tirateunpaso.ui.components

import TirateUnPasoTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HeaderText(
    text : String,
    modifier : Modifier = Modifier
) {
    TirateUnPasoTheme {

    Text(
        text = text,
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
        color = MaterialTheme.colorScheme.tertiary
    )
    }
}