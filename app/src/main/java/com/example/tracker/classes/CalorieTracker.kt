package com.example.tracker.classes

import java.time.LocalDate

class Food(name: String, date: LocalDate, val calories: Int) : ItemBase(name, date)

class CalorieTracker : TrackerBase<Food>()