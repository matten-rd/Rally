package com.example.compose.rally.ui.accounts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.compose.rally.R
import com.example.compose.rally.nav.DialogScreen
import com.example.compose.rally.ui.components.AccountRow
import com.example.compose.rally.ui.components.RallyAlertDialogDeleteAccount
import com.example.compose.rally.ui.components.StatementBody
import com.example.compose.rally.utils.ColorConverter

/**
 * The Accounts screen.
 */
@ExperimentalFoundationApi
@Composable
fun AccountsBody(navController: NavController, viewModel: AccountsViewModel) {

    val accountsFromVM = viewModel.accounts.observeAsState()

    var currentSelectedItem by remember { mutableStateOf(accountsFromVM.value?.get(0)) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        currentSelectedItem?.let {
            RallyAlertDialogDeleteAccount(
                onDismiss = { showDialog = false },
                bodyText = it.name,
                account = it,
                viewModel = viewModel
            )
        }
    }


    accountsFromVM.value?.let { accounts ->
        StatementBody(
            items = accounts,
            amounts = { account -> account.balance },
            colors = { account -> ColorConverter.getColor(account.colorHEX) },
            amountsTotal = accounts.map { account -> account.balance }.sum(),
            circleLabel = stringResource(R.string.total),
            buttonLabel = DialogScreen.NewAccount.route,
            navController = navController,
            onLongPress = {
                currentSelectedItem = it
                showDialog = true
            },
            rows = { account, _ ->
                AccountRow(
                    name = account.name,
                    bank = account.bank,
                    amount = account.balance,
                    color = ColorConverter.getColor(account.colorHEX),
                    account = account,
                    onClick = {
                        navController.currentBackStackEntry?.arguments?.putParcelable("account", account)
                        navController.navigate(DialogScreen.UpdateAccount.route)
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




