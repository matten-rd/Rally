package com.example.compose.rally.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.compose.rally.ui.theme.DarkGrey
import com.example.compose.rally.ui.theme.LightGrey


/**
 * A pill shaped switch displaying two options in text format.
 */
@Composable
fun RallySwitch(
    checked: Boolean,
    text1: String,
    text2: String,
    onClick: () -> Unit
) {
    val modifier = Modifier
        .clip(RoundedCornerShape(15.dp))
        .fillMaxHeight()

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onClick)
            .height(30.dp)
            .background(DarkGrey)
    ) {
        Box(
            modifier = if (checked) modifier.background(LightGrey) else modifier
        ) {
            Text(
                text = text1,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .align(Alignment.Center),
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = if (!checked) modifier.background(LightGrey) else modifier
        ) {
            Text(
                text = text2,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .align(Alignment.Center),
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold
            )
        }

    }
}


/**
 * A pill shaped button that expand into a DropdownMenu radioButtons and text.
 */
@Composable
fun RallyDropdownMenu(
    filterTitle: String,
    radioOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = { expanded = true })
            .height(30.dp)
            .background(LightGrey)
    ) {
        Text(
            text = filterTitle,
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .align(Alignment.Center),
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            radioOptions.forEach { text ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(text)
                    expanded = false
                }) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Text(text = text, style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}