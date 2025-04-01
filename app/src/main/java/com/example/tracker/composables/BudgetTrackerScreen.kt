package com.example.tracker.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.example.tracker.DataBase.TrackerViewModel

@Composable
fun BudgetTrackerScreen(viewModel: TrackerViewModel) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            "Budget Tracker"
        )
    }
}