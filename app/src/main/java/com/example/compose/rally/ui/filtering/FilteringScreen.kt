package com.example.compose.rally.ui.filtering


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.rally.data.bill.BillData
import com.example.compose.rally.ui.bills.BillViewModel
import com.example.compose.rally.ui.components.*
import com.example.compose.rally.ui.theme.DarkBlue900
import com.example.compose.rally.ui.theme.Green500
import com.example.compose.rally.ui.theme.Red300
import com.example.compose.rally.utils.ColorConverter
import kotlin.math.absoluteValue

/**
 * A monthly overview screen with bar chart and list of transactions
 */
@ExperimentalFoundationApi
@Composable
fun FilterBody(
    navController: NavController,
    viewModel: BillViewModel
) {
    // TabLayout titles and states
    val tabTitles = viewModel.getTabTitles()
    val stateInt = tabTitles?.lastIndex
    var tabState by remember { mutableStateOf(stateInt) }

    val billsFilteredByMonth = viewModel.getBillsFilteredByMonth(tabTitles!![tabState!!])

    val netto = billsFilteredByMonth!!.sumOf { it.amount.toDouble() }.toFloat()

    val billsFilteredByCategory = viewModel.getBillsFilteredByCategory(billsFilteredByMonth)

    var checkedState by remember { mutableStateOf(true) } // Month or category state

    val radioOptions = listOf("Allt", "Inkomster", "Utgifter")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    val filterState =
        if (checkedState) {
            when (selectedOption) {
                radioOptions[0] -> FilterState.ALL_MONTH
                radioOptions[1] -> FilterState.INCOME_MONTH
                radioOptions[2] -> FilterState.EXPENSE_MONTH
                else -> FilterState.ALL_MONTH
            }
        } else {
            when (selectedOption) {
                radioOptions[0] -> FilterState.ALL_CATEGORY
                radioOptions[1] -> FilterState.INCOME_CATEGORY
                radioOptions[2]-> FilterState.EXPENSE_CATEGORY
                else -> FilterState.ALL_MONTH
            }
        }

    val (filteredList, icon, color) = when(filterState) {
        FilterState.ALL_MONTH -> {
            Triple(
                billsFilteredByMonth,
                if (netto > 0) Icons.Filled.AttachMoney else Icons.Filled.MoneyOff,
                Color.White
            )
        }

        FilterState.INCOME_MONTH -> {
            Triple(
                viewModel.getBillsFilteredByAmountMonth(true, billsFilteredByMonth),
                Icons.Filled.AttachMoney,
                Green500
            )
        }

        FilterState.EXPENSE_MONTH -> {
            Triple(
                viewModel.getBillsFilteredByAmountMonth(false, billsFilteredByMonth),
                Icons.Filled.MoneyOff,
                Red300
            )
        }

        FilterState.ALL_CATEGORY -> {
            Triple(
                billsFilteredByCategory,
                if (netto > 0) Icons.Filled.AttachMoney else Icons.Filled.MoneyOff,
                Color.White
            )
        }

        FilterState.INCOME_CATEGORY -> {
            Triple(
                viewModel.getBillsFilteredByAmountCategory(true, billsFilteredByCategory),
                Icons.Filled.AttachMoney,
                Green500
            )
        }

        FilterState.EXPENSE_CATEGORY -> {
            Triple(
                viewModel.getBillsFilteredByAmountCategory(false, billsFilteredByCategory),
                Icons.Filled.MoneyOff,
                Red300
            )
        }
    }

    val amount = filteredList?.sumOf { it.amount.toDouble() }?.toFloat() ?: 0f

    val billsProportions = filteredList?.extractProportions { it.amount.absoluteValue }
    val colors = filteredList?.map { ColorConverter.getColor(it.colorHEX) }
    val uniqueColors = colors!!.distinct()

    // FIXME: This can be done in some better way
    val pair = colors.zip(billsProportions!!)
    val group = pair.groupBy { it.first }
    val hell = group.mapKeys { entry ->
        entry.value.sumOf {
            it.second.toDouble()
        }
    }
    val floatProportions = hell.keys.toList().map { it.toFloat() }
    val maxOfAll = floatProportions.maxOrNull() ?: floatProportions.sum() // get max (or sum)
    val percentProportions = floatProportions.map { (it/maxOfAll) }


    // The actual composable
    FilterBaseBody(
        proportions = percentProportions,
        colors = uniqueColors,
        amount = amount,
        billsFiltered = filteredList,
        icon = icon,
        color = color,
        checked = checkedState,
        onCheckedChange = { checkedState = !checkedState },
        onOptionSelected = onOptionSelected,
        selectedOption = selectedOption,
        radioOptions = radioOptions
    ) {
        tabState?.let {
            ScrollableTabRow(
                selectedTabIndex = it,
                edgePadding = 100.dp,
                divider = { Divider(thickness = 0.dp) },
                indicator = { Divider(thickness = 0.dp, color = Color.Transparent) },
                backgroundColor = DarkBlue900
            ) {
                tabTitles.forEachIndexed { index, title ->
                    RallyMonthTab(
                        title = title,
                        selected = (tabState == index),
                        onClick = { tabState = index }
                    )
                }
            }
        }
    }

}


enum class FilterState {
    ALL_MONTH, ALL_CATEGORY,
    INCOME_MONTH, INCOME_CATEGORY,
    EXPENSE_MONTH, EXPENSE_CATEGORY
}

@ExperimentalFoundationApi
@Composable
fun FilterBaseBody(
    proportions: List<Float>,
    colors: List<Color>,
    amount: Float,
    billsFiltered: List<BillData>?,
    icon: ImageVector,
    color: Color,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    onOptionSelected: (String) -> Unit,
    selectedOption: String,
    radioOptions: List<String>,
    tabs: @Composable () -> Unit
) {
    Column {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RallySwitch(
                checked = checked,
                text1 = "Månad",
                text2 = "Kategori",
                onClick = onCheckedChange
            )

            RallyDropdownMenu(
                filterTitle = selectedOption,
                radioOptions = radioOptions,
                selectedOption = selectedOption,
                onOptionSelected = onOptionSelected
            )
        }

        AnimatedBarchart(
            proportions = proportions,
            colors = colors,
            modifier = Modifier
                .height(200.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        )

        Divider(
            color = Color.White.copy(alpha = 0.5f),
            thickness = 0.5.dp,
            modifier = Modifier.padding(top = 0.5.dp)
        )

        NetExpenseIncomeRow(
            amount = amount,
            icon = icon,
            color = color
        )

        tabs()

        if (checked)
            CardLazyColumn(
                billsFiltered = billsFiltered,
                modifier = Modifier.padding(12.dp)
            )
        else
            CardLazyColumnCategory(
                billsFiltered = billsFiltered,
                modifier = Modifier.padding(12.dp)
            )

    }
}



/**
 * A centered text and icon to display based on state - income, expense or all.
 */
@Composable
fun NetExpenseIncomeRow(
    amount: Float,
    icon: ImageVector,
    color: Color,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = color
            )
            Text(text = "${formatAmount(amount)} kr",
                color = color, style = MaterialTheme.typography.h6)
        }
    }
}

/**
 * A LazyColumn within a Card to display a list of BillData.
 */
@ExperimentalFoundationApi
@Composable
fun CardLazyColumnCategory(
    billsFiltered: List<BillData>?,
    modifier: Modifier = Modifier
) {
    billsFiltered?.let { bills ->
        val totalExpenses = bills.filter { it.amount <= 0f }.sumOf { it.amount.toDouble() }.toFloat()
        val totalIncome = bills.filter { it.amount > 0f }.sumOf { it.amount.toDouble() }.toFloat()
        Card {
            LazyColumn(modifier = modifier) {
                items(bills) { bill ->
                    CategoryRow(
                        category = bill.category,
                        amount = bill.amount,
                        color = ColorConverter.getColor(bill.colorHEX),
                        bill = bill,
                        totalExpenses = totalExpenses,
                        totalIncome = totalIncome
                    )
                }
            }
        }
    }
}

/**
 * A LazyColumn within a Card to display a list of BillData.
 */
@ExperimentalFoundationApi
@Composable
fun CardLazyColumn(
    billsFiltered: List<BillData>?,
    modifier: Modifier = Modifier
) {
    billsFiltered?.let { bills ->
        Card {
            LazyColumn(modifier = modifier) {
                items(bills) { bill ->
                    BillRow(
                        name = bill.comment ?: "Inkomst",
                        date = bill.date,
                        category = bill.category,
                        amount = bill.amount,
                        color = ColorConverter.getColor(bill.colorHEX),
                        bill = bill,
                        onClick = {  },
                        onLongPress = {  }
                    )
                }
            }
        }
    }
}


/**
 * A custom tab for scrollable tab layout.
 */
@Composable
fun RallyMonthTab(title: String, selected: Boolean, onClick: () -> Unit) {
    Tab(
        selected = selected,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(horizontal = 40.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = if( selected ) Color.White else Color.White.copy(0.2f)
            )
        }
    }
}




