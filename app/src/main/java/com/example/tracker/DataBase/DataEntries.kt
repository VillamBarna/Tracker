package com.example.tracker.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "foods")
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val calories: Int,
    val date: String
)

@Entity(tableName = "profiles")
data class ProfileEntry(
    @PrimaryKey val id: Int = 1,
    val calorieGoal: Int = 2000
)