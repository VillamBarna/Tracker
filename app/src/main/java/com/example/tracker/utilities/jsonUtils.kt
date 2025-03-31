package com.example.tracker.utilities

import android.content.Context
import com.example.tracker.classes.Food
import org.json.JSONArray
import java.time.LocalDateTime

fun readJsonArrayFromAssets(context: Context, fileName: String): JSONArray {
    val file  = context.assets.open(fileName).bufferedReader().use { it.readText()}
    val jsonObject = JSONArray(file)
    return jsonObject
}

fun readFoodsJson(context: Context, fileName: String): List<Food> {
    val listOfFoods = mutableListOf<Food>()
    val jsonOfFoods = readJsonArrayFromAssets(context, fileName)
    for (i in 0 until jsonOfFoods.length()) {
        val jsonFood = jsonOfFoods.getJSONObject(i)
        listOfFoods.add(Food(jsonFood.getString("name"), LocalDateTime.now(), jsonFood.getInt("calories")))
    }
    return listOfFoods
}