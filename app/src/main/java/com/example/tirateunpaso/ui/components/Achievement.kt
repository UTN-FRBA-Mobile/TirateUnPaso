package com.example.tirateunpaso.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tirateunpaso.ui.values

@Composable
fun Achievement() {
    
}

data class Content(
    val id:Int,
    val title:String,
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
            transition.animateFloat (label = "icon_change"){
                state ->
                if(state){
                    0f
                }else{
                    180f
                }
            }

    Card() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = values.defaultPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = content.title)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(iconRotationDeg)
                        .clickable {
                            onClickExpanded(content.id)
                        }
                )
            }
            Spacer(modifier = Modifier.size(values.defaultSpacing))
            ExpandableContent(
                isExpanded = expanded,
                desc = content.desc
            )
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
        Text(text = desc, textAlign = TextAlign.Justify)
    }
}