

package com.example.compose.rally.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import java.util.*
import kotlin.math.absoluteValue

/**
 * Generic component used by the accounts and bills screens to show a chart and a list of items.
 */
@Composable
fun <T> StatementBody(
    items: List<T>,
    colors: (T) -> Color,
    amounts: (T) -> Float,
    amountsTotal: Float,
    circleLabel: String,
    buttonLabel: String,
    navController: NavController,
    onLongPress: (T) -> Unit,
    rows: @Composable (T, (T) -> Unit) -> Unit
) {
    Column {
        // Animating circle and balance box
        Box(Modifier.padding(16.dp)) {
            val accountsProportion = items.extractProportions { amounts(it).absoluteValue }
            val circleColors = items.map { colors(it) }
            val uniqueColors = circleColors.distinct() // takes only the unique colors

            // FIXME: This can be done in some better way
            val pair = circleColors zip accountsProportion
            val group = pair.groupBy { it.first }
            val hell = group.mapKeys { entry ->
                entry.value.sumByDouble {
                    it.second.toDouble()
                }
            }
            val floatProportions = hell.keys.toList().map { it.toFloat() }

            AnimatedCircle(
                floatProportions,
                uniqueColors,
                Modifier
                    .height(300.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = circleLabel,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = formatAmount(amountsTotal),
                    style = MaterialTheme.typography.h2
                )
                Button(onClick = { navController.navigate(buttonLabel) }) {
                    Text(text = buttonLabel.toUpperCase(Locale.getDefault()))
                }
            }
        }
        Spacer(Modifier.height(10.dp))

        // Recycler view
        Card {
            LazyColumn(modifier = Modifier.padding(12.dp)) {
                itemsIndexed(items) { idx, item ->
                    rows(item, onLongPress)  // rows is the Composable you pass in the constructor
                }
            }
        }

    }
}
