package com.example.compose.rally.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.compose.rally.data.account.AccountData
import com.example.compose.rally.data.bill.BillData
import com.example.compose.rally.ui.accounts.AccountsViewModel
import com.example.compose.rally.ui.bills.BillViewModel
import com.example.compose.rally.ui.theme.RallyDialogThemeOverlay

@Composable
fun RallyAlertDialogGeneral(
    onDismiss: () -> Unit,
    bodyText: String,
    buttonText: String
) {
    RallyDialogThemeOverlay {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = { Text(text = bodyText) },
            buttons = {
                Divider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                )
                TextButton(
                    onClick = onDismiss,
                    shape = RectangleShape,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = buttonText)
                }
            }
        ) 
    }
}

/**
 * An alert dialog that is used as confirmation to delete a savings account.
 */
@Composable
fun RallyAlertDialogDeleteAccount(
    onDismiss: () -> Unit,
    bodyText: String,
    account: AccountData,
    viewModel: AccountsViewModel
) {
    RallyDialogThemeOverlay {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Är du säker på att du vill radera kontot: \"${bodyText}\"?")
                }
            },
            buttons = {
                Column() {
                    Divider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            shape = RectangleShape,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(text = "STÄNG")
                        }

                        TextButton(
                            onClick = {
                                viewModel.deleteAccount(account)
                            },
                            shape = RectangleShape,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(text = "RADERA", color = MaterialTheme.colors.error)
                        }
                    }
                }
            }
        )
    }
}

/**
 * An alert dialog that is used as confirmation to delete some transaction.
 */
@Composable
fun RallyAlertDialogDeleteBill(
    onDismiss: () -> Unit,
    bodyText: String,
    bill: BillData,
    viewModel: BillViewModel
) {
    RallyDialogThemeOverlay {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Är du säker på att du vill radera transaktionen: \"${bodyText}\"?")
                }
            },
            buttons = {
                Column() {
                    Divider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            shape = RectangleShape,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(text = "STÄNG")
                        }

                        TextButton(
                            onClick = {
                                viewModel.deleteBill(bill)
                            },
                            shape = RectangleShape,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(text = "RADERA", color = MaterialTheme.colors.error)
                        }
                    }
                }
            }
        )
    }
}




