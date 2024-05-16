package com.example.tirateunpaso.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tirateunpaso.ui.values

@Composable
fun LoginFooter(
    onClick:() -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    buttonText : String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = text,
                fontSize = values.fontsize
            )
            TextButton(
                onClick = onClick
            ) {
                Text(
                    text = buttonText,
                    fontSize = values.fontsize
                )
            }
        }
    }
}