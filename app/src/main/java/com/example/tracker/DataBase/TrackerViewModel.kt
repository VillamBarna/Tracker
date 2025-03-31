package com.example.tracker.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TrackerViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.foodDao()

    val entries: StateFlow<List<FoodEntry>> = dao.getALLEntries()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun saveToDatabase(text: String, date: String, calories: Int) {
        viewModelScope.launch {
            dao.insert(FoodEntry(name = text, date = date, calories = calories))
        }
    }

    fun deleteItem(item: FoodEntry) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }
}

class TrackerViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrackerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}