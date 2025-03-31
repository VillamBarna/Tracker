package com.example.tracker.classes

import java.time.LocalDateTime

class Food(name: String, date: LocalDateTime, val calories: Int) : ItemBase(name, date)

class CalorieTracker : TrackerBase<Food>()