package com.example.compose.rally.ui.bills

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.compose.rally.data.bill.BillData
import com.example.compose.rally.nav.Screen
import com.example.compose.rally.nav.Tabs
import com.example.compose.rally.ui.components.ColorPickerTransaction
import com.example.compose.rally.ui.components.DropdownTransaction
import com.example.compose.rally.ui.components.ReadonlyTextField
import com.example.compose.rally.ui.theme.*
import com.example.compose.rally.utils.ColorConverter
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.datepicker
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue


/**
 * Tab layout and handle "navigation"
 */
@Composable
fun Transaction(
    viewModel: BillViewModel,
    navController: NavController,
    bill: BillData?
) {
    val tabTitles = Tabs.values().map { it.route }
    var stateInt = Tabs.Expense.ordinal // Starting point
    if (bill != null) { // If update
        if (bill.comment == null) // If update income
            stateInt = Tabs.Income.ordinal
    }
    var tabState by remember { mutableStateOf(stateInt) }


    Column {
        TabRow(
            selectedTabIndex = tabState
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title) },
                    selected = (tabState == index),
                    onClick = { tabState = index }
                )
            }
        }
        Crossfade(targetState = tabState) {
            when(it) {
                Tabs.Expense.ordinal -> {
                    if (bill != null)
                        UpdateExpense(bill, viewModel, navController)
                    else
                        AddNewExpense(viewModel, navController)
                }
                Tabs.Income.ordinal -> {
                    if (bill != null)
                        UpdateIncome(bill, viewModel, navController)
                    else
                        AddNewIncome(viewModel, navController)
                }
            }
        }

    }
}

/**
 * Handle Expenses - Update and new ones
 */

@Composable
fun UpdateExpense(
    bill: BillData,
    viewModel: BillViewModel,
    navController: NavController
) {
    val title = "Uppdatera"
    val expense = bill.amount.absoluteValue.toString()
    val comment = bill.comment
    val color = ColorConverter.getColor(bill.colorHEX)
    val date = bill.date
    ExpenseInput(
        bill = bill,
        expense = expense,
        comment = comment!!,
        color = color,
        date = date,
        title = title,
        viewModel = viewModel,
        navController = navController
    )

}

@Composable
fun AddNewExpense(
    viewModel: BillViewModel,
    navController: NavController
) {
    val title = "Ny utgift"
    ExpenseInput(
        bill = null,
        expense = "",
        comment = "",
        color = null,
        date = "",
        title = title,
        viewModel = viewModel,
        navController = navController
    )
}

/**
 * Base expense input screen.
 */
@Composable
fun ExpenseInput(
    bill: BillData?,
    expense: String,
    comment: String,
    color: Color?,
    date: String,
    title: String,
    viewModel: BillViewModel,
    navController: NavController
) {
    val categories = listOf(
        "Transport", "Tele & Prenum", "Mat & Dryck",
        "Shopping", "Hälsa", "Skola", "Övrigt"
    )
    val colors = listOf(
        Red400, Red300, Red200, Yellow500, Yellow300, Yellow200, Red50
    )
    val (selectedColor, onColorSelected) = remember { mutableStateOf(color ?: colors[0]) }

    val items = categories zip colors

    var newExpense by remember { mutableStateOf(expense) }
    var newComment by remember { mutableStateOf(comment) }
    val context = LocalContext.current

    val dialog = MaterialDialog()
    val formattedDate = LocalDate.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
    var newDate by remember { mutableStateOf(if (date != "") date else formattedDate) }

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Card {
            Column(Modifier.padding(RallyDefaultPadding)) {
                Column {
                    Text(text = title, style = MaterialTheme.typography.h2)
                }

                Divider(
                    modifier = Modifier.padding(vertical = RallyDefaultPadding),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                )

                DropdownTransaction(
                    items = items,
                    selectedColor = selectedColor,
                    onColorSelected = onColorSelected
                )
                Spacer(modifier = Modifier.height(RallyDefaultPadding))

                OutlinedTextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    label = { Text(text = "Kommentar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(RallyDefaultPadding))

                OutlinedTextField(
                    value = newExpense,
                    onValueChange = { newExpense = it },
                    label = { Text(text = "Utgift") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(RallyDefaultPadding))

                // Date picker in textfield
                dialog.build {
                    datepicker { date ->
                        val selectedFormattedDate = date.format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )
                        newDate = selectedFormattedDate
                    }
                }
                ReadonlyTextField( // See components/DatePicker file
                    value = newDate,
                    onValueChange = { newDate = it },
                    onClick = { dialog.show() },
                    label = { Text(text = "Välj datum") },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Today, contentDescription = null) },
                    trailingIcon = {  }
                )


                Spacer(modifier = Modifier.height(RallyDefaultPadding))
                Button(
                    onClick = {
                        if(newExpense.isNotEmpty() && newComment.isNotEmpty()) {
                            val chosenCategory = items.find { it.second == selectedColor }?.first
                            saveIncomeAndNavigateBack(
                                bill = bill,
                                comment = newComment,
                                category = chosenCategory!!,
                                date = newDate,
                                amount = "-$newExpense",
                                colorHEX = ColorConverter.getHex(selectedColor),
                                viewModel = viewModel,
                                navController = navController
                            )
                        } else {
                            Toast.makeText(context, "Vänligen fyll i alla fält", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "SPARA")
                }
            }
        }
    }

}

/**
 * Handle Incomes - Update and new ones
 */

@Composable
fun UpdateIncome(
    bill: BillData,
    viewModel: BillViewModel,
    navController: NavController
) {
    val title = "Uppdatera"
    val income = bill.amount.toString()
    val date = bill.date
    val color = ColorConverter.getColor(bill.colorHEX)
    IncomeInput(
        bill = bill,
        income = income,
        color = color,
        date = date,
        title = title,
        viewModel = viewModel,
        navController = navController
    )
}

@Composable
fun AddNewIncome(
    viewModel: BillViewModel,
    navController: NavController
) {
    val title = "Ny inkomst"
    IncomeInput(
        bill = null,
        income = "",
        color = null,
        date = "",
        title = title,
        viewModel = viewModel,
        navController = navController
    )
}

/**
 * Base expense input screen.
 * Note that category is not necessary to pass in as it maps directly to some color.
 */
@Composable
fun IncomeInput(
    bill: BillData?,
    income: String,
    color: Color?,
    date: String,
    title: String,
    viewModel: BillViewModel,
    navController: NavController
) {
    val categories = listOf("Lön", "CSN", "Gåva", "Övrigt")
    val colors = listOf(DarkGreen700, DarkGreen600, Green500, Green300)

    val (selectedColor, onColorSelected) = remember { mutableStateOf(color ?: colors[0]) }

    val items = categories zip colors

    var newIncome by remember { mutableStateOf(income) }
    val context = LocalContext.current

    val dialog = MaterialDialog()
    val formattedDate = LocalDate.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
    var newDate by remember { mutableStateOf(if (date != "") date else formattedDate) }

    Column(modifier = Modifier.padding(16.dp)) {
        Card {
            Column(Modifier.padding(RallyDefaultPadding)) {
                Column {
                    Text(text = title, style = MaterialTheme.typography.h2)
                }

                Divider(
                    modifier = Modifier.padding(vertical = RallyDefaultPadding),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                )

                DropdownTransaction(
                    items = items,
                    selectedColor = selectedColor,
                    onColorSelected = onColorSelected
                )

                Spacer(modifier = Modifier.height(RallyDefaultPadding))

                OutlinedTextField(
                    value = newIncome,
                    onValueChange = { newIncome = it },
                    label = { Text(text = "Inkomst") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(RallyDefaultPadding))

                // Date picker in textfield
                dialog.build {
                    datepicker { date ->
                        val selectedFormattedDate = date.format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )
                        newDate = selectedFormattedDate
                    }
                }
                ReadonlyTextField( // See components/DatePicker file
                    value = newDate,
                    onValueChange = { newDate = it },
                    onClick = { dialog.show() },
                    label = { Text(text = "Välj datum") },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Today, contentDescription = null) },
                    trailingIcon = {  }
                )


                Spacer(modifier = Modifier.height(RallyDefaultPadding))
                Button(
                    onClick = {
                        if(newIncome.isNotEmpty() ) {
                            val chosenCategory = items.find { it.second == selectedColor }?.first
                            saveIncomeAndNavigateBack(
                                bill = bill,
                                comment = null,
                                category = chosenCategory!!,
                                date = newDate,
                                amount = newIncome,
                                colorHEX = ColorConverter.getHex(selectedColor),
                                viewModel = viewModel,
                                navController = navController
                            )
                        } else {
                            Toast.makeText(context, "Vänligen fyll i alla fält", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "SPARA")
                }
            }
        }
    }
}

// I think this will work for both income and expense
private fun saveIncomeAndNavigateBack(
    bill: BillData?,
    comment: String?,
    category: String,
    date: String,
    amount: String,
    colorHEX: String,
    viewModel: BillViewModel,
    navController: NavController
) {

    if (bill != null) {
        val updatedTransaction = bill.copy(
            comment = comment,
            category = category,
            date = date,
            amount = amount.toFloatOrNull() ?: 123f,
            colorHEX = colorHEX
        )
        viewModel.updateBill(updatedTransaction)
    } else {
        val newTransaction =  BillData(
            comment = comment,
            category = category,
            date = date,
            amount = amount.toFloatOrNull() ?: 123f,
            colorHEX = colorHEX
        )
        viewModel.createBill(newTransaction)
    }
    navController.navigate(Screen.Transaktion.route)
}





private val RallyDefaultPadding = 12.dp