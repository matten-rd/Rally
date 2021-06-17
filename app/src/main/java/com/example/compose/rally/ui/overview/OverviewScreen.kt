package com.example.compose.rally.ui.overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.compose.rally.R
import com.example.compose.rally.nav.Screen
import com.example.compose.rally.ui.accounts.AccountsViewModel
import com.example.compose.rally.ui.bills.BillViewModel
import com.example.compose.rally.ui.components.*
import com.example.compose.rally.utils.ColorConverter
import kotlin.math.absoluteValue

@ExperimentalFoundationApi
@Composable
fun OverviewBody(
    navController: NavController,
    accountsViewModel: AccountsViewModel,
    billViewModel: BillViewModel
) {
    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        AccountsCard(navController, accountsViewModel)
        Spacer(Modifier.height(RallyDefaultPadding))
        BillsCard(navController, billViewModel)
        Spacer(Modifier.height(RallyDefaultPadding))
        MonthCard(navController, billViewModel)
    }
}

/**
 * Base structure for cards in the Overview screen.
 */
@Composable
private fun <T> OverviewScreenCard(
    title: String,
    amount: Float,
    onClickSeeAll: () -> Unit,
    values: (T) -> Float,
    colors: (T) -> Color,
    data: List<T>,
    row: @Composable (T) -> Unit
) {
    Card {
        Column {

            Column(Modifier.padding(RallyDefaultPadding)) {
                Text(text = title, style = MaterialTheme.typography.subtitle2)
                val amountText = formatAmount(amount) + " kr"
                Text(text = amountText, style = MaterialTheme.typography.h2)
            }

            OverViewDivider(data, values, colors)

            Column(Modifier.padding(start = 16.dp, top = 4.dp, end = 8.dp)) {
                data.take(SHOWN_ITEMS).forEach { row(it) }

                SeeAllButton(onClick = onClickSeeAll)
            }
        }
    }
}

@Composable
private fun <T> OverViewDivider(
    data: List<T>,
    values: (T) -> Float,
    colors: (T) -> Color
) {
    val proportions = data.extractProportions { values(it).absoluteValue }
    val notUnColors = data.map { colors(it) }
    val unColors = notUnColors.distinct()

    // FIXME: This can be done in some better way
    val pair = notUnColors zip proportions
    val group = pair.groupBy { it.first }
    val hell = group.mapKeys { it1 ->
        it1.value.sumByDouble {
            it.second.toDouble()
        }
    }
    val floatProportions = hell.keys.toList().map { it.toFloat() }

    Row(Modifier.fillMaxWidth()) {
        floatProportions.forEachIndexed { index, proportion ->
            Spacer(
                modifier = Modifier
                    .weight(proportion.absoluteValue)
                    .height(1.dp)
                    .background(unColors[index])
            )
        }
        /*
        data.forEach { item: T ->
            Spacer(
                modifier = Modifier
                    .weight(values(item).absoluteValue)
                    .height(1.dp)
                    .background(colors(item))
            )
        }
        */
    }
}

/**
 * The Accounts card within the Rally Overview screen.
 */
@ExperimentalFoundationApi
@Composable
private fun AccountsCard(navController: NavController, accountsViewModel: AccountsViewModel) {
    val accountsFromVM = accountsViewModel.accounts.observeAsState()

    accountsFromVM.value?.let { accounts ->
        val amount = accounts.map { account -> account.balance }.sum()

        OverviewScreenCard(
            title = stringResource(R.string.accounts),
            amount = amount,
            onClickSeeAll = { navController.navigate(Screen.Spara.route) },
            data = accounts,
            colors = { ColorConverter.getColor(it.colorHEX) },
            values = { it.balance }
        ) { account ->
            AccountRow(
                name = account.name,
                bank = account.bank,
                amount = account.balance,
                color = ColorConverter.getColor(account.colorHEX),
                account = account,
                onClick = { },
                onLongPress = { }
            )
        }
    }
}

/**
 * The Bills card within the Rally Overview screen.
 */
@ExperimentalFoundationApi
@Composable
private fun BillsCard(navController: NavController, billViewModel: BillViewModel) {
    val billsFromVM = billViewModel.bills.observeAsState()

    billsFromVM.value?.let { bills ->
        val amount = bills.map { bill -> bill.amount }.sum()

        OverviewScreenCard(
            title = stringResource(R.string.bills),
            amount = amount,
            onClickSeeAll = { navController.navigate(Screen.Transaktion.route) },
            data = bills,
            colors = { ColorConverter.getColor(it.colorHEX) },
            values = { it.amount }
        ) { bill ->
            BillRow(
                name = bill.comment ?: "Inkomst",
                date = bill.date,
                category = bill.category,
                amount = bill.amount,
                color = ColorConverter.getColor(bill.colorHEX),
                bill = bill,
                onClick = { },
                onLongPress = { }
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun MonthCard(navController: NavController, billViewModel: BillViewModel) {
    val billsFromVM = billViewModel.bills.observeAsState()

    billsFromVM.value?.let { bills ->
        val formattedMonthList = billViewModel.getBillsFormattedToMonthRow(bills)

        OverviewScreenCard(
            title = "Månadsvis",
            amount = formattedMonthList.first().sumByMonth,
            onClickSeeAll = { navController.navigate(Screen.Filter.route) },
            values = { it.sumByMonth },
            colors = { ColorConverter.getColor(it.colorHEX) },
            data = formattedMonthList
        ) { monthData ->
            MonthRow(
                month = monthData.month,
                amountByMonth = monthData.sumByMonth,
                expenses = monthData.monthExpenses,
                income = monthData.monthIncome,
                color = ColorConverter.getColor(monthData.colorHEX),
                dataType = monthData
            )
        }
    }


}


@Composable
private fun SeeAllButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.see_all))
    }
}

private val RallyDefaultPadding = 12.dp

private const val SHOWN_ITEMS = 3
