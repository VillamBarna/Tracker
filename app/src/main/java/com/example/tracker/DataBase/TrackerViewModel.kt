package com.example.tracker.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.tracker.classes.Food
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackerViewModel(application: Application) : AndroidViewModel(application) {
    //private val db = AppDatabase.getDatabase(application)
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "your_database_name"
    ).fallbackToDestructiveMigration().build()
    private val foodDao = db.foodDao()
    private val profileDao = db.profileDao()

    val foodsInDatabase: StateFlow<List<FoodEntry>> = foodDao.getALLEntries()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val calorieGoalInDatabase: StateFlow<Int> = profileDao.getCalorieGoal()
        .stateIn(viewModelScope, SharingStarted.Lazily, 2000 )

    fun saveFood(food: Food) {
        viewModelScope.launch {
            foodDao.insert(FoodEntry(name = food.name, date = food.date.toString(), calories = food.calories))
        }
    }

    fun deleteFood(item: FoodEntry) {
        viewModelScope.launch {
            foodDao.delete(item)
        }
    }

    fun saveProfile(calorieGoal: Int) {
        viewModelScope.launch {
            profileDao.saveProfile(ProfileEntry(calorieGoal = calorieGoal))
        }
    }

    fun getProfile() = liveData {
        emit(profileDao.getProfile())
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