package com.example.tracker.DataBase

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    suspend fun insert(entry: FoodEntry)

    @Query("SELECT * FROM foods")
    fun getALLEntries(): Flow<List<FoodEntry>>

    @Delete
    suspend fun delete(item: FoodEntry)
}

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: ProfileEntry)

    @Query("SELECT * FROM profiles WHERE id = 1 LIMIT 1")
    fun getProfile(): ProfileEntry?

    @Query("SELECT calorieGoal FROM profiles WHERE id = 1 LIMIT 1")
    fun getCalorieGoal(): Flow<Int>
}