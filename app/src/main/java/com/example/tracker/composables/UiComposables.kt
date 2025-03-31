package com.example.tracker.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tracker.DataBase.FoodEntry
import com.example.tracker.DataBase.TrackerViewModel
import java.time.LocalDateTime

@Composable
fun MainScreen(viewModel: TrackerViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        FoodInputButton(viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        FoodList(viewModel)
    }
}

@Composable
fun FoodList(viewModel: TrackerViewModel) {
    val foods by viewModel.entries.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(foods) { food ->
            TwoItemRow(food, viewModel)
        }
    }
}

@Composable
fun TwoItemRow(food: FoodEntry, viewModel: TrackerViewModel) {
    var confirmDelete by remember { mutableStateOf<FoodEntry?>(null) } // Track item for deletion

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { confirmDelete = food },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = food.name)
        Text(text = food.calories.toString())
    }

    confirmDelete?.let { item ->
        ConfirmDeleteDialog(
            item = item,
            onConfirm = {
                viewModel.deleteItem(item)
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
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
fun FoodInputButton(viewModel: TrackerViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }
    var numberInput by remember { mutableStateOf("") }

    Column {
        Button(onClick = { showDialog = true }) {
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
                            Text("Enter food data", style=MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))

                            TextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                label = { Text("food's name")}
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = numberInput,
                                onValueChange = { if (it.all { c -> c.isDigit() }) numberInput = it },
                                label = { Text("calories")}
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton (onClick = {showDialog = false}) {
                                    Text("Cancel")
                                }
                                TextButton(onClick = {
                                    viewModel.saveToDatabase(textInput, LocalDateTime.now().toString(), numberInput.toInt())
                                    showDialog = false}) {
                                    Text("Save")
                                }
                            }

                        }
                    }
                }
            )
        }
    }
}