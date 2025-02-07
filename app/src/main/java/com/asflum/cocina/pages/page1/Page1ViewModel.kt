package com.asflum.cocina.pages.page1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asflum.cocina.SavedConfig
import com.asflum.cocina.SavedConfigDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Ayuda de la IA para implementar este ViewModel
class FoodViewModel(private val savedConfigDao: SavedConfigDao) : ViewModel() {

    // Un flujo de datos observable por la UI
    private val _foodList = MutableStateFlow<List<SavedConfig>>(emptyList())
    val foodList: StateFlow<List<SavedConfig>> = _foodList

    // Función para consultar la base de datos
    fun getAllFoodInfo() {
        viewModelScope.launch {
            val data = savedConfigDao.getAllFoodInfo()
            _foodList.value = data
        }
    }
}