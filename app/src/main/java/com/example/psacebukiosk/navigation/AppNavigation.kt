package com.example.psacebukiosk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.psacebukiosk.screens.CitizensCharterScreen
import com.example.psacebukiosk.screens.CivilRegistryScreen
import com.example.psacebukiosk.screens.HomeScreen
import com.example.psacebukiosk.screens.NationalIdScreen
import com.example.psacebukiosk.screens.OfficeGuideScreen
import com.example.psacebukiosk.screens.PublicBulletinScreen
import com.example.psacebukiosk.screens.StatisticsScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object CivilRegistry : Screen("civil_registry")
    data object NationalId : Screen("national_id")
    data object CitizensCharter : Screen("citizens_charter")
    data object PublicBulletin : Screen("public_bulletin")
    data object OfficeGuide : Screen("office_guide")
    data object Statistics : Screen("statistics")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.CivilRegistry.route) {
            CivilRegistryScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.NationalId.route) {
            NationalIdScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.CitizensCharter.route) {
            CitizensCharterScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.PublicBulletin.route) {
            PublicBulletinScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.OfficeGuide.route) {
            OfficeGuideScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
