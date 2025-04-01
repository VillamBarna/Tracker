package com.example.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.tracker.DataBase.TrackerViewModel
import com.example.tracker.DataBase.TrackerViewModelFactory
import com.example.tracker.ui.theme.TrackerTheme
import com.example.tracker.composables.CalorieTrackerScreen
import com.example.tracker.composables.NavigationDrawer

class MainActivity : ComponentActivity() {
    private val viewModel: TrackerViewModel by viewModels {
        TrackerViewModelFactory(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            TrackerTheme(darkTheme = true) {
                NavigationDrawer(viewModel, navController)
            }
        }
    }
}
