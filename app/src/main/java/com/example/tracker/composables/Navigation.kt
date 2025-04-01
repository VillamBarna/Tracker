package com.example.tracker.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tracker.DataBase.TrackerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(viewModel: TrackerViewModel, navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState, scope)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tracker")},
                    colors = TopAppBarColors(   containerColor = Color.DarkGray,
                                                titleContentColor = Color.White,
                                                navigationIconContentColor = Color.White,
                                                actionIconContentColor = Color.White,
                                                scrolledContainerColor = Color.White),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            Box(Modifier.padding(padding)) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(viewModel) }
                    composable("settings") { SettingsScreen(navController) }
                    composable("calorieTracker") { CalorieTrackerScreen(viewModel) }
                    composable("budgetTracker") { BudgetTrackerScreen(viewModel) }
                    composable("settings_main") { SettingsScreen(navController) }
                    composable("settings_profile") { ProfileSettingsScreen(viewModel) }
                    composable("settings_app") { AppSettingsScreen() }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    ModalDrawerSheet {
        Text(
            "Navigation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()

        DrawerItem("Home", "home", navController, drawerState, scope, Icons.Default.Home)
        DrawerItem("Calorie tracker", "calorieTracker", navController, drawerState, scope, Icons.Default.Face)
        DrawerItem("Budget tracker", "budgetTracker", navController, drawerState, scope, Icons.Default.ShoppingCart)
        DrawerItem("Settings", "settings", navController, drawerState, scope, Icons.Default.Settings)
    }
}

@Composable
fun DrawerItem(title: String, route: String, navController: NavController, drawerState: DrawerState, scope: CoroutineScope, icon: ImageVector) {
    Row(
        modifier = Modifier
            .clickable {
                navController.navigate(route)
                scope.launch { drawerState.close() }
            }
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.padding(end = 8.dp))
        Text(
            title,
            fontSize = 18.sp,
        )
    }
}