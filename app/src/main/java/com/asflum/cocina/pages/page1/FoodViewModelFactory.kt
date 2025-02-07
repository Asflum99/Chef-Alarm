package com.asflum.cocina.pages.page1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asflum.cocina.database.SavedConfigDao

// Ayuda de la IA para implementar este Factory del ViewModel
class FoodViewModelFactory(private val savedConfigDao: SavedConfigDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            return FoodViewModel(savedConfigDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}