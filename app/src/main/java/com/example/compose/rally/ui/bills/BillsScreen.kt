package com.example.compose.rally.ui.bills

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.compose.rally.R
import com.example.compose.rally.nav.DialogScreen
import com.example.compose.rally.ui.components.BillRow
import com.example.compose.rally.ui.components.RallyAlertDialogDeleteAccount
import com.example.compose.rally.ui.components.RallyAlertDialogDeleteBill
import com.example.compose.rally.ui.components.StatementBody
import com.example.compose.rally.utils.ColorConverter

/**
 * The Bills screen.
 */

@ExperimentalFoundationApi
@Composable
fun BillsBody(navController: NavController, viewModel: BillViewModel) {
    val billsFromVM = viewModel.bills.observeAsState()

    var currentSelectedItem by remember { mutableStateOf(billsFromVM.value?.get(0)) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        currentSelectedItem?.let {
            RallyAlertDialogDeleteBill(
                onDismiss = { showDialog = false },
                bodyText = it.category,
                bill = it,
                viewModel = viewModel
            )
        }
    }


    billsFromVM.value?.let { bills ->
        StatementBody(
            items = bills,
            amounts = { bill -> bill.amount },
            colors = { bill -> ColorConverter.getColor(bill.colorHEX) },
            amountsTotal = bills.map { bill -> bill.amount }.sum(),
            circleLabel = stringResource(R.string.due),
            buttonLabel = DialogScreen.NewTransaction.route,
            navController = navController,
            onLongPress = {
                currentSelectedItem = it
                showDialog = true
            },
            rows = { bill, _ ->
                BillRow(
                    name = bill.comment ?: "Inkomst",
                    date = bill.date,
                    category = bill.category,
                    amount = bill.amount,
                    color = ColorConverter.getColor(bill.colorHEX),
                    bill = bill,
                    onClick = {
                        navController.currentBackStackEntry?.arguments?.putParcelable("bill", bill)
                        navController.navigate(DialogScreen.UpdateTransaction.route)
                    },
                    onLongPress = {
                        currentSelectedItem = it
                        showDialog = true
                    }
                )
            }
        )
    }
}
