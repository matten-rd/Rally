package com.example.compose.rally.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compose.rally.data.account.AccountData
import com.example.compose.rally.data.bill.BillData
import com.example.compose.rally.data.month.MonthData
import java.text.DecimalFormat
import kotlin.math.absoluteValue

/**
 * A row representing the basic information of an Account.
 */
@ExperimentalFoundationApi
@Composable
fun AccountRow(
    name: String,
    bank: String,
    amount: Float,
    color: Color,
    account: AccountData,
    onClick: () -> Unit,
    onLongPress: (AccountData) -> Unit
) {
    BaseRow(
        color = color,
        title = name,
        subtitle = bank,
        amount = amount,
        rowType = account,
        onClick = onClick,
        onLongPress = onLongPress
    )
}

/**
 * A row representing the basic information of a Bill.
 */
@ExperimentalFoundationApi
@Composable
fun BillRow(
    name: String,
    date: String,
    category: String,
    amount: Float,
    color: Color,
    bill: BillData,
    onClick: () -> Unit,
    onLongPress: (BillData) -> Unit
) {
    BaseRow(
        color = color,
        title = name,
        subtitle = "$category • $date",
        amount = amount,
        rowType = bill,
        onClick = onClick,
        onLongPress = onLongPress
    )
}

@ExperimentalFoundationApi
@Composable
fun CategoryRow(
    category: String,
    amount: Float,
    color: Color,
    bill: BillData,
    totalExpenses: Float?,
    totalIncome: Float?
) {
    val percent = if (amount > 0 && totalIncome != null) {
        amount*100/totalIncome
    } else if (amount <= 0 && totalExpenses != null) {
        amount.absoluteValue*100/totalExpenses.absoluteValue
    } else {
        0f
    }
    BaseRow(
        color = color,
        title = category,
        subtitle = "${formatAmount(percent)}% av " + if (amount > 0) "inkomster" else "utgifter",
        amount = amount,
        rowType = bill,
        onClick = {  },
        onLongPress = {  }
    )
}

@ExperimentalFoundationApi
@Composable
fun MonthRow(
    month: String,
    amountByMonth: Float,
    expenses: Float,
    income: Float,
    color: Color,
    dataType: MonthData
) {
    BaseRow(
        color = color,
        title = month,
        subtitle = "In ${formatAmount(income)} kr • Ut ${formatAmount(expenses)} kr",
        amount = amountByMonth,
        rowType = dataType,
        onClick = {  },
        onLongPress = {  }
    )
}

@ExperimentalFoundationApi
@Composable
private fun <T> BaseRow(
    color: Color,
    title: String,
    subtitle: String,
    amount: Float,
    rowType: T,
    onClick: () -> Unit,
    onLongPress: (T) -> Unit
) {
    val formattedAmount = formatAmount(amount)
    Row(
        modifier = Modifier
            .height(68.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = { onLongPress(rowType) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val typography = MaterialTheme.typography

        AccountIndicator(color = color, modifier = Modifier)
        Spacer(Modifier.width(12.dp))

        Column(Modifier) {
            Text(text = title, style = typography.body1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = subtitle, style = typography.subtitle1)
            }
        }

        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "$formattedAmount kr",
                style = typography.h6,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(Modifier.width(16.dp))

    }
    RallyDivider()
}

/**
 * A vertical colored line that is used in a [BaseRow] to differentiate accounts.
 */
@Composable
private fun AccountIndicator(color: Color, modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .size(4.dp, 36.dp)
            .background(color = color)
    )
}

@Composable
fun RallyDivider(modifier: Modifier = Modifier) {
    Divider(color = MaterialTheme.colors.background, thickness = 1.dp, modifier = modifier)
}

fun formatAmount(amount: Float): String {
    return AmountDecimalFormat.format(amount)
}

private val AmountDecimalFormat = DecimalFormat("#,###")

/**
 * Used with accounts and bills to create the animated circle.
 */
fun <E> List<E>.extractProportions(selector: (E) -> Float): List<Float> {
    val total = this.sumOf { selector(it).toDouble() }
    return this.map { (selector(it) / total).toFloat() }
}


