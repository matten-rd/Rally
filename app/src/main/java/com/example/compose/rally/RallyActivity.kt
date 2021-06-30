package com.example.compose.rally

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.data.account.AccountData
import com.example.compose.rally.data.bill.BillData
import com.example.compose.rally.nav.DialogScreen
import com.example.compose.rally.nav.Screen
import com.example.compose.rally.nav.items
import com.example.compose.rally.ui.accounts.AccountsBody
import com.example.compose.rally.ui.accounts.AccountsViewModel
import com.example.compose.rally.ui.accounts.AddNewAccount
import com.example.compose.rally.ui.accounts.UpdateAccount
import com.example.compose.rally.ui.bills.BillViewModel
import com.example.compose.rally.ui.bills.BillsBody
import com.example.compose.rally.ui.bills.Transaction
import com.example.compose.rally.ui.components.RallyTab
import com.example.compose.rally.ui.filtering.FilterBody
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.ui.theme.RallyTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class RallyActivity : AppCompatActivity() {
    private val accountsViewModel: AccountsViewModel by viewModels()
    private val billViewModel: BillViewModel by viewModels()

    @ExperimentalAnimationApi
    @ExperimentalUnsignedTypes
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RallyTheme {
                // TODO: show splash screen here I think

                RallyApp(accountsViewModel, billViewModel)
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalUnsignedTypes
@ExperimentalFoundationApi
@Composable
fun RallyApp(accountsViewModel: AccountsViewModel, billViewModel: BillViewModel) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            Surface(
                Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                Row(Modifier.selectableGroup()) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    items.forEach { screen ->
                        RallyTab(
                            text = screen.label.uppercase(Locale.getDefault()),
                            icon = screen.icon,
                            selected = (currentRoute == screen.route),
                            onSelected = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        //SplashScreen(navController = navController)
        Box(modifier = Modifier.padding(innerPadding)) {

            NavHost(navController = navController, startDestination = Screen.Hem.route) {

                //composable(Screen.Splash.route) { SplashScreen(navController) }

                // Main navigation
                composable(Screen.Hem.route) { OverviewBody(navController, accountsViewModel, billViewModel) }
                composable(Screen.Spara.route) { AccountsBody(navController, accountsViewModel) }
                composable(Screen.Transaktion.route) { BillsBody(navController, billViewModel) }
                composable(Screen.Filter.route) { FilterBody(navController, billViewModel) }

                // Other screens
                composable(DialogScreen.NewAccount.route) { AddNewAccount(navController, accountsViewModel) }
                composable(DialogScreen.UpdateAccount.route) {
                    var account = navController.previousBackStackEntry?.arguments?.getParcelable<AccountData>("account")

                    UpdateAccount(
                        account = account,
                        viewModel = accountsViewModel,
                        navController = navController
                    )
                }

                composable(DialogScreen.NewTransaction.route) { Transaction(billViewModel, navController, null) }
                composable(DialogScreen.UpdateTransaction.route) {
                    var bill = navController.previousBackStackEntry?.arguments?.getParcelable<BillData>("bill")

                    Transaction(
                        viewModel = billViewModel,
                        navController = navController,
                        bill = bill
                    )
                }
            }
        }


    }

}

