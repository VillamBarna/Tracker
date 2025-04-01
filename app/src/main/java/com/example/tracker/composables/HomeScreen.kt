package com.example.tracker.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.example.tracker.DataBase.TrackerViewModel
import java.time.LocalDate
import java.util.Locale

@Composable
fun HomeScreen(viewModel: TrackerViewModel) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            DailyCalorieProgress(viewModel)
        }
    }
}

@Composable
fun DailyCalorieProgress(viewModel: TrackerViewModel) {
    var progress by remember { mutableFloatStateOf(0f) }
    val foods by viewModel.foodsInDatabase.collectAsState()
    val calorieGoal by viewModel.calorieGoalInDatabase.collectAsState()
    var caloriesConsumed = 0
    val today = LocalDate.now()


    for (food in foods) {
        val foodDay = LocalDate.parse(food.date)
        if (foodDay == today)
            caloriesConsumed += food.calories
    }
    if (calorieGoal == 0) {
        progress = 1f
    }
    else {
        progress = caloriesConsumed.toFloat()/calorieGoal.toFloat()
    }

    Column(
        modifier = Modifier.padding(6.dp)
    ) {
        Text(
            "Daily Calorie Intake",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
        LinearProgressIndicator(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(10.dp),
            progress = { progress },
            color = Color.Green
        )
        Text(
            text = String.format(Locale.UK, "%d/%d", caloriesConsumed, calorieGoal),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
