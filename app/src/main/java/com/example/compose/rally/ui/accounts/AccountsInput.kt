package com.example.compose.rally.ui.accounts

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.rally.data.account.AccountData
import com.example.compose.rally.nav.Screen
import com.example.compose.rally.ui.components.ColorPicker
import com.example.compose.rally.ui.theme.Green300
import com.example.compose.rally.ui.theme.Green500
import com.example.compose.rally.ui.theme.DarkGreen700
import com.example.compose.rally.ui.theme.DarkGreen600
import com.example.compose.rally.utils.ColorConverter

/**
 * A screen to add a new savings account.
 */
@ExperimentalUnsignedTypes
@Composable
fun AddNewAccount(
    navController: NavController,
    viewModel: AccountsViewModel
) {
    val title = "Nytt sparkonto"
    AccountInput(
        account = null,
        title = title,
        name = "",
        bank = "",
        balance = "",
        color = null,
        viewModel = viewModel,
        navController = navController
    )
}

/**
 * A screen to update a savings account.
 */
@Composable
fun UpdateAccount(
    account: AccountData?,
    viewModel: AccountsViewModel,
    navController: NavController
) {
    val title = "Uppdatera"
    val name = account?.name ?: ""
    val bank = account?.bank ?: ""
    val balance = account?.balance.toString()
    val color = account?.let { ColorConverter.getColor(it.colorHEX) }
    AccountInput(
        account = account,
        title = title,
        name = name,
        bank = bank,
        balance = balance,
        color = color,
        viewModel = viewModel,
        navController = navController
    )
}

/**
 * Template screen for creating/updating a savings account
 */
@Composable
fun AccountInput(
    account: AccountData?,
    title: String,
    name: String,
    bank: String,
    balance: String,
    color: Color?,
    viewModel: AccountsViewModel,
    navController: NavController
) {
    var newName by remember { mutableStateOf(name) }
    var newBank by remember { mutableStateOf(bank) }
    var newBalance by remember { mutableStateOf(balance) }
    val context = LocalContext.current

    val colors = listOf(
        DarkGreen700,
        DarkGreen600,
        Green500,
        Green300
    )
    val (selectedColor, onColorSelected) = remember { mutableStateOf(color ?: colors[0]) }

    Column(modifier = Modifier.padding(16.dp)) {
        Card {
            Column {
                Column(Modifier.padding(RallyDefaultPadding)) {
                    Text(text = title, style = MaterialTheme.typography.h2)
                }

                Divider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                )

                Column(modifier = Modifier
                    .padding(RallyDefaultPadding)
                    .fillMaxWidth()) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text(text = "Namnge sparkontot") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(RallyDefaultPadding))

                    OutlinedTextField(
                        value = newBank,
                        onValueChange = { newBank = it },
                        label = { Text(text = "Sparplattform") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(RallyDefaultPadding))

                    OutlinedTextField(
                        value = newBalance,
                        onValueChange = { newBalance = it },
                        label = { Text(text = "Pengar på kontot just nu") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(RallyDefaultPadding))

                    Text(text = "Välj färg för sparkontot", color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(RallyDefaultPadding))

                    ColorPicker(
                        items = colors,
                        selectedColor = selectedColor,
                        onColorSelected = onColorSelected
                    )
                }
                Button(
                    onClick = {
                        if(newName.isNotEmpty() && newBank.isNotEmpty() && newBalance.isNotEmpty()) {
                            saveAndNavigateBack(
                                account = account,
                                name = newName,
                                bank = newBank,
                                balance = newBalance,
                                colorHEX = ColorConverter.getHex(selectedColor),
                                viewModel = viewModel,
                                navController = navController
                            )
                        } else {
                            Toast.makeText(context, "Vänligen fyll i alla fält", Toast.LENGTH_SHORT).show()
                            // TODO: Show snackbar instead (unclear how to do this)
                        }
                    },
                    modifier = Modifier
                        .padding(RallyDefaultPadding)
                        .fillMaxWidth()
                ) {
                    Text(text = "SPARA")
                }
            }
        }
    }
}


private fun saveAndNavigateBack(
    account: AccountData?,
    name: String,
    bank: String,
    balance: String,
    colorHEX: String,
    viewModel: AccountsViewModel,
    navController: NavController
) {
    // TODO: To some kind of input validation (isError in the TextFields above)

    if (account != null) {
        val updatedAccount = account.copy(
            name = name,
            bank = bank,
            balance = balance.toFloatOrNull() ?: 123f,
            colorHEX = colorHEX
        )
        viewModel.updateAccount(updatedAccount)
    } else {
        val newAccount = AccountData(
            name = name,
            bank = bank,
            balance = balance.toFloatOrNull() ?: 123f,
            colorHEX = colorHEX
        )
        viewModel.createAccount(newAccount)
    }
    navController.navigate(Screen.Spara.route)
}





private val RallyDefaultPadding = 12.dp