package com.example.tirateunpaso.ui.components
import TirateUnPasoTheme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tirateunpaso.R

data class Content(
    val id:Int,
    val title:String,
    val unlocked: Boolean,
    val desc:String
)

const val EXPANSION_ANIMATION_DURATION = 300

@Composable
fun ExpandableCard(
    content: Content,
    expanded: Boolean,
    onClickExpanded:(id:Int) -> Unit
) {
    val transition = updateTransition(targetState = expanded, label = "transition")

    val iconRotationDeg by
    transition.animateFloat(label = "icon_change") { state ->
        if (state) {
            0f
        } else {
            180f
        }
    }
    TirateUnPasoTheme { //envolver con TirateUnPasoTheme que es la función. Después se puede usar MaterialTheme.colorScheme para los colores
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background) // mismo color que las cards de la parte de estadísticas
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val trophyId: Int = if (content.unlocked) {
                        R.drawable.trofeo_colores
                    } else {
                        R.drawable.trofeo_byn
                    }
                    Image(
                        painter = painterResource(id = trophyId),
                        contentDescription = "Achievement trophy",
                        modifier = Modifier
                            .weight(2f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = content.title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(7f),
                        maxLines = 3
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth()
                            .weight(1f)
                            .background(color = MaterialTheme.colorScheme.primary)
                            .wrapContentSize(align = Alignment.CenterEnd),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            modifier = Modifier
                                .rotate(iconRotationDeg)
                                .wrapContentSize(align = Alignment.CenterEnd)
                                .clickable {
                                    onClickExpanded(content.id)
                                }
                        )
                    }
                }
                //Spacer(modifier = Modifier.size(values.defaultSpacing))
                ExpandableContent(
                    isExpanded = expanded,
                    desc = content.desc
                )
            }
        }
    }
}

@Composable
fun ExpandableContent(
    isExpanded: Boolean,
    desc: String
) {
    val enterTransition = remember{
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(EXPANSION_ANIMATION_DURATION)
        ) + fadeIn(
            initialAlpha = .3f,
            animationSpec = tween(EXPANSION_ANIMATION_DURATION)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(EXPANSION_ANIMATION_DURATION)
        ) + fadeOut(
            animationSpec = tween(EXPANSION_ANIMATION_DURATION)
        )
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = desc)
        }
    }
}