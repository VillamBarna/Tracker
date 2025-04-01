package com.example.tracker.composables

import android.content.Context
import android.hardware.DataSpace
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tracker.DataBase.FoodEntry
import com.example.tracker.DataBase.TrackerViewModel
import com.example.tracker.utilities.readFoodsJson
import java.time.LocalDate


@Composable
fun CalorieTrackerScreen(viewModel: TrackerViewModel) {
    var selectedDay by remember { mutableStateOf(LocalDate.now()) }
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            DateSelectionMenu(viewModel, selectedDay) {newDay -> selectedDay = newDay}
            Spacer(modifier = Modifier.height(16.dp))
            FoodInputButton(viewModel, selectedDay)
            Spacer(modifier = Modifier.height(20.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Name")
                Text("Calories[kcal]")
            }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(6.dp))
            FoodList(viewModel, selectedDay)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                HorizontalDivider()
                BottomPart(viewModel, selectedDay)
            }
        }
    }
}

@Composable
fun BottomPart(viewModel: TrackerViewModel, selectedDay: LocalDate) {
    val foods by viewModel.foodsInDatabase.collectAsState()
    var caloriesConsumed = 0
    for (food in foods) {
        val foodDay = LocalDate.parse(food.date)
        if (foodDay == selectedDay)
            caloriesConsumed += food.calories
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Sum of calories")
        Text(caloriesConsumed.toString())
    }
}

@Composable
fun FoodList(viewModel: TrackerViewModel, selectedDay: LocalDate) {
    val foods by viewModel.foodsInDatabase.collectAsState()
    val filteredFoods = foods.filter { food -> LocalDate.parse(food.date) == selectedDay }
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredFoods) { food ->
            TwoItemRow(food, viewModel)
        }
    }
}

@Composable
fun TwoItemRow(food: FoodEntry, viewModel: TrackerViewModel) {
    var confirmDelete by remember { mutableStateOf<FoodEntry?>(null) } // Track item for deletion

    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 10.dp,
        modifier = Modifier
            .clickable { confirmDelete = food },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = food.name)
            Text(text = food.calories.toString())
        }
    }

    confirmDelete?.let { item ->
        ConfirmDeleteDialog(
            item = item,
            onConfirm = {
                viewModel.deleteFood(item)
                confirmDelete = null
            },
            onDismiss = { confirmDelete = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDeleteDialog(item: FoodEntry, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    BasicAlertDialog (
        onDismissRequest = onDismiss,
        content = {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 6.dp,
                modifier = Modifier.padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Confirm Deletion", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        TextButton(onClick = onConfirm) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInputButton(viewModel: TrackerViewModel, selectedDay: LocalDate) {
    var showDialog by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var foodOptions = readFoodsJson(LocalContext.current, "foods.json")
    foodOptions = foodOptions.sortedBy { food -> food.name }
    val filteredOptions = foodOptions.filter { it.name.contains(searchText, ignoreCase = true) }

    Column {
        Button(onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()) {
            Text("Add food")
        }

        if(showDialog) {
            BasicAlertDialog(
                onDismissRequest = { showDialog = false},
                content = {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 6.dp,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Add food", style=MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))

                            TextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                label = { Text("Search Food")}
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Text( text = "Name" )
                                Text( text = "Calories" )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyColumn (
                                modifier = Modifier.height(300.dp)
                            ) {
                                items(filteredOptions) { food ->
                                    Surface(
                                        tonalElevation = 10.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                food.date = selectedDay
                                                viewModel.saveFood(food)
                                                showDialog = false
                                            }) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = food.name)
                                            Text(text = food.calories.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionMenu(viewModel: TrackerViewModel, selectedDay: LocalDate, changeSelectedDay: (LocalDate) -> Unit) {
    val today = LocalDate.now()
    val days = (0..30).map { today.minusDays(it.toLong()) }
    var expanded by remember { mutableStateOf(false) }

    Surface(tonalElevation = 10.dp) {
        Box(
            modifier = Modifier
                .padding(3.dp)
        ) {
            Column {
                Text("Select Day to edit")
                HorizontalDivider()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(16.dp)
                        .fillMaxWidth())
                {
                    Text(
                        text = selectedDay.toString()
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(300.dp),
                    onDismissRequest = { expanded = false }
                ) {
                    days.forEach() { day ->
                        DropdownMenuItem(
                            text = {
                                Text(text = day.toString())
                            },
                            onClick = {
                                changeSelectedDay(day)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}