package com.example.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tracker.DataBase.TrackerViewModel
import com.example.tracker.DataBase.TrackerViewModelFactory
import com.example.tracker.ui.theme.TrackerTheme
import com.example.tracker.classes.Food
import com.example.tracker.classes.CalorieTracker
import com.example.tracker.composables.FoodInputButton
import com.example.tracker.composables.FoodList
import com.example.tracker.composables.MainScreen
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    private val viewModel: TrackerViewModel by viewModels {
        TrackerViewModelFactory(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackerTheme(darkTheme = true) {
                MainScreen(viewModel)
            }
        }
    }
}
