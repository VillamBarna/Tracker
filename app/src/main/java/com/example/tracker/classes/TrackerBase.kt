package com.example.tracker.classes

abstract class TrackerBase<T : ItemBase> {
    val items = mutableListOf<T>()

    fun addItem(item: T) {
        items.add(item)
    }

    fun getItemNames(): List<String> {
        val names = mutableListOf<String>()
        items.forEach { names.add(it.name) }
        return names
    }
}