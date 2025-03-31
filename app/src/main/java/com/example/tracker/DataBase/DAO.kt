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