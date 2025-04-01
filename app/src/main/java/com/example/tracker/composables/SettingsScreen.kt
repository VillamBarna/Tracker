package com.example.tracker.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.example.tracker.DataBase.TrackerViewModel

@Composable
fun SettingsScreen(navController: NavHostController) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            SettingsMenuItem(navController, "Profile Settings", "settings_profile", Icons.Default.AccountCircle)
            SettingsMenuItem(navController, "App Settings", "settings_app", Icons.Default.Build)
        }
    }
}

@Composable
fun SettingsMenuItem(navController: NavHostController, title: String, route: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { navController.navigate(route) }
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title)
    }
}

@Composable
fun ProfileSettingsScreen(viewModel: TrackerViewModel) {
    val calorieGoal by viewModel.calorieGoalInDatabase.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Text(
                "Profile Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .clickable { showDialog = true }
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Calorie Goal [kcal]")
                Text(calorieGoal.toString())
            }
        }
    }
    if (showDialog) {
        CalorieEnterPopup(viewModel, onDismiss = { showDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieEnterPopup(viewModel: TrackerViewModel, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }
    BasicAlertDialog(
        onDismissRequest = {onDismiss()},
        content = {
            Surface(
                tonalElevation = 6.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text("New daily calorie goal",
                        modifier = Modifier.padding(vertical = 5.dp))
                    TextField(
                        value = text,
                        onValueChange = { newText ->
                            if (newText.all { it.isDigit() })
                                text = newText
                        },
                        label = { Text("Enter calorie goal") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            val number = text.toIntOrNull()
                            if (number != null) {
                                viewModel.saveProfile(number)
                                onDismiss()
                            }
                        }
                    ) { Text( "Submit" ) }
                }
            }
        }
    )
}

@Composable
fun AppSettingsScreen() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Text( "App settings" )
    }
}

