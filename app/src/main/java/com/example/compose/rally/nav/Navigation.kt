package com.example.compose.rally.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class Tabs(val route: String) {
    Expense(route = "Utgift"),
    Income(route = "Inkomst")
}

sealed class DialogScreen(val route: String) {
    object NewAccount : DialogScreen("Nytt sparande")
    object UpdateAccount : DialogScreen("Uppdatera sparande")
    object NewTransaction : DialogScreen("Ny transaktion")
    object UpdateTransaction : DialogScreen("Uppdatera transaktion")
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Splash : Screen("splash", "SplashScreen", Icons.Filled.Public)
    object Hem : Screen("hem", "Sammandrag", Icons.Filled.PieChart)
    object Spara : Screen("spara", "Sparande", Icons.Filled.AttachMoney)
    object Transaktion : Screen("transaktion", "Transaktioner", Icons.Filled.MoneyOff)
    object Filter : Screen("filter", "Filter", Icons.Filled.BarChart)
}
val items = listOf(
    Screen.Hem,
    Screen.Spara,
    Screen.Transaktion,
    Screen.Filter
)