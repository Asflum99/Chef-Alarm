package com.asflum.cocina.pages.page2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Page2ViewModel: ViewModel() {

    // Estado para el alimento seleccionado
    private val _selectedFood = MutableStateFlow("Seleccione alimento")
    val selectedFood: StateFlow<String> get() = _selectedFood

    // Estado para la selección de medición
    private val _selectedMeasurement = MutableStateFlow("Tipo de medición")
    val selectedMeasurement: StateFlow<String> get() = _selectedMeasurement

    private val _selectedCook = MutableStateFlow("Tipo de cocción")
    val selectedCook: StateFlow<String> get() = _selectedCook

    private val _selectedPotato = MutableStateFlow("Tamaño de papa")
    val selectedPotato: StateFlow<String> get() = _selectedPotato

    private val _selectedRice = MutableStateFlow("Cantidad")
    val selectedRice: StateFlow<String> get() = _selectedRice

    private val _selectedSpaghetti = MutableStateFlow("Textura")
    val selectedSpaghetti: StateFlow<String> get() = _selectedSpaghetti

    private val _timeCalculated = MutableStateFlow(0)
    val timeCalculated: StateFlow<Int> get() = _timeCalculated

    fun updateSelectedFood(food: String) {
        _selectedFood.value = food
    }

    fun updateSelectedMeasurement(measurement: String) {
        _selectedMeasurement.value = measurement
    }

    fun updateSelectedCook(cook: String) {
        _selectedCook.value = cook
    }

    fun updateSelectedPotato(potato: String) {
        _selectedPotato.value = potato
    }

    fun updateSelectedRice(rice: String) {
        _selectedRice.value = rice
    }

    fun updateSelectedSpaghetti(spaghetti: String) {
        _selectedSpaghetti.value = spaghetti
    }

    fun setTimeCalculated(time: Int) {
        _timeCalculated.value = time
    }

    fun changeTimeCalculated() {
        _timeCalculated.value += (_timeCalculated.value * 20) / 100
    }

    fun riceTimeCalculated(option: String, options: List<String>) {
        _timeCalculated.value = when (option) {
            options[0] -> {
                11
            }
            options[1] -> {
                15
            }
            else -> {
                18
            }
        }
    }

    fun spaghettiTimeCalculated(option: String) {
        _timeCalculated.value = when (option) {
            "Al dente" -> {
                8
            }
            else -> {
                10
            }
        }
    }

    fun minusTimeCalculated() {
        _timeCalculated.value -= 1
    }

    fun plusTimeCalculated() {
        _timeCalculated.value += 1
    }

    fun timeCalculatedToZero() {
        _timeCalculated.value = 0
    }
}