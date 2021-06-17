package com.example.compose.rally.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.rally.ui.theme.DarkBlue900
import kotlin.math.exp

/**
 * A standard color picker displaying the options in a row.
 */
@Composable
fun ColorPicker(
    items: List<Color>,
    selectedColor: Color,
    onColorSelected: (color: Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row {
            items.distinct().forEach { color ->
                ColorItem(
                    selected = (color == selectedColor),
                    color = color,
                    onClick = { onColorSelected(color) }
                )
            }
        }
    }
}

/**
 * Filled circle used in color picker.
 * Selected style based on the selected color attributes.
 */
@Composable
fun ColorItem(
    selected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(CircleShape)
            .size(40.dp)
            .clickable(onClick = onClick)
    ) {

        Box(modifier = Modifier
            .width(20.dp)
            .fillMaxHeight()
            .background(DarkBlue900))

        val colorModifier =
            if (color.luminance() < 0.1 || color.luminance() > 0.9) {
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onSurface,
                        shape = CircleShape
                    )
            } else {
                Modifier
                    .fillMaxSize()
                    .background(color)
            }

        Box(modifier = colorModifier) {
            if(selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    tint = if (color.luminance() < 0.5) Color.White else Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    contentDescription = null
                )
            }
        }
    }

}


/**
 * A color picker where the color also maps to some string.
 * Used for transactions.
 */
@Composable
fun ColorPickerTransaction(
    items: List<Pair<String, Color>>,
    selectedColor: Color,
    onColorSelected: (color: Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column {
            items.distinct().forEach { (category, color) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ColorItem(
                        selected = (color == selectedColor),
                        color = color,
                        onClick = { onColorSelected(color) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = category, style = MaterialTheme.typography.h6, fontSize = 20.sp)
                    }
                }

            }
        }
    }
}


/**
 * A color picker where the color also maps to some string.
 * Used for transactions.
 */
@Composable
fun DropdownTransaction(
    items: List<Pair<String, Color>>,
    selectedColor: Color,
    onColorSelected: (color: Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val initialCategory = items.find { it.second == selectedColor }!!.first
    var newCategory by remember { mutableStateOf(initialCategory) }
    val rotate: Float by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    Column(modifier = modifier.fillMaxWidth()) {
        ReadonlyTextField(
            value = newCategory,
            onValueChange = { newCategory = it },
            onClick = { expanded = true },
            label = { Text(text = "Välj kategori") },
            modifier = modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Circle, contentDescription = null, tint = selectedColor)
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotate)
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier.fillMaxWidth()
        ) {
            items.distinct().forEach { (category, color) ->
                DropdownMenuItem(
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        newCategory = category
                        onColorSelected(color)
                        expanded = false
                    }
                ) {
                    ColorItem(
                        selected = (color == selectedColor),
                        color = color,
                        onClick = {
                            newCategory = category
                            onColorSelected(color)
                            expanded = false
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = category, fontSize = 18.sp, modifier = modifier.fillMaxWidth())
                }
            }
        }
    }

}



