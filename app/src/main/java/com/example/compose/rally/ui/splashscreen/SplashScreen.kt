package com.example.compose.rally.ui.splashscreen


import android.os.Handler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.compose.rally.ui.theme.DarkGreen600
import com.example.compose.rally.ui.theme.DarkGreen700
import com.example.compose.rally.ui.theme.Green300
import com.example.compose.rally.ui.theme.Green500


@ExperimentalAnimationApi
@Composable
fun SplashScreen() {

    val currentState = remember {
        MutableTransitionState(AnimatedSplashScreen.START)
            .apply { targetState = AnimatedSplashScreen.END }
    }
    val stroke = with(LocalDensity.current) { Stroke(40.dp.toPx()) }
    val transition = updateTransition(transitionState = currentState, label = "")
    val angleOffset by transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 1500,
                easing = LinearOutSlowInEasing
            )
        }, label = ""
    ) { progress ->
        when(progress) {
            AnimatedSplashScreen.START -> 0f
            AnimatedSplashScreen.END -> 360f
        }
    }
    val shift by transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 1500,
                easing = CubicBezierEasing(0f, 0.75f, 0.35f, 0.85f)
            )
        }, label = ""
    ) { progress ->
        when(progress) {
            AnimatedSplashScreen.START -> 0f
            AnimatedSplashScreen.END -> 30f
        }
    }
    val infiniteTransition = rememberInfiniteTransition()
    val positionState by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -250f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                delayMillis = SplashScreenDelay,
                easing = LinearOutSlowInEasing
            )
        )
    )

    val proportions = listOf<Float>(0.25f, 0.25f, 0.25f, 0.25f)
    val colors = listOf<Color>(Green500, Green300, DarkGreen600, DarkGreen700)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .offset(0.dp, positionState.dp)
    ) {
        val innerRadius = (1.6f * stroke.width)
        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius - positionState
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = shift - 120f
        proportions.forEachIndexed { index, proportion ->
            val sweep = proportion * angleOffset
            drawArc(
                color = colors[index],
                startAngle = startAngle,
                sweepAngle = sweep,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke
            )
            startAngle += sweep
        }
    }

    delayAndNavigate()
}
private const val SplashScreenDuration = 3000L
private const val SplashScreenDelay = 2000
private enum class AnimatedSplashScreen { START, END }

private fun delayAndNavigate() {
    val handler = Handler()
    handler.postDelayed(Runnable {

    }, SplashScreenDuration)
}