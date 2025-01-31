package com.asflum.cocina

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asflum.cocina.pages.page1.FoodViewModel

class FoodViewModelFactory(private val savedConfigDao: SavedConfigDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            return FoodViewModel(savedConfigDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}