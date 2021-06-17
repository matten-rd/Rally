package com.example.compose.rally.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

/**
 * A bar chart that animates when loading.
 */
@Composable
fun AnimatedBarchart(
    proportions: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    // TODO: Somehow reset this state every tab click
    val currentState = remember {
        MutableTransitionState(AnimatedBarchartProgress.START)
            .apply { targetState = AnimatedBarchartProgress.END }
    }

    val transition = updateTransition(transitionState = currentState, label = "")
    val percentAnimate by transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = LinearOutSlowInEasing
            )
        }, label = ""
    ) { state ->
        when(state) {
            AnimatedBarchartProgress.START -> 0f
            AnimatedBarchartProgress.END -> 1f
        }
    }

    Canvas(
        modifier = modifier
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val numberOfBars = proportions.size

        proportions.forEachIndexed { index, percent ->
            val barHeight = ( canvasHeight*percent*percentAnimate )
            val barWidth = ( canvasWidth/(numberOfBars*2 + 1) )
            val yOffset = canvasHeight-barHeight
            val xOffset = ( barWidth * (index*2 + 1) )

            drawRect(
                color = colors[index],
                size = Size(barWidth, barHeight),
                topLeft = Offset(x = xOffset, y = yOffset)
            )
        }
    }

}

private enum class AnimatedBarchartProgress { START, END }