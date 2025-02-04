package com.asflum.cocina.pages.page2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt

class Page2ViewModel: ViewModel() {

    // Selección del alimento
    private val _selectedFood = MutableStateFlow("Seleccione alimento")
    val selectedFood: StateFlow<String> get() = _selectedFood

    // Selección de la medición
    private val _selectedMeasurement = MutableStateFlow("Tipo de medición")
    val selectedMeasurement: StateFlow<String> get() = _selectedMeasurement

    // Selección de la cocción
    private val _selectedCook = MutableStateFlow("Tipo de cocción")
    val selectedCook: StateFlow<String> get() = _selectedCook

    // Variables de papa
    private val _selectedPotato = MutableStateFlow("Tamaño de papa") // Selección de tamaño
    val selectedPotato: StateFlow<String> get() = _selectedPotato
    private val _selectedTypePotato = MutableStateFlow("Tipo de papa") // Tipo de papa
    val selectedTypePotato: StateFlow<String> get() = _selectedTypePotato
    private val _selectedCutTypePotato = MutableStateFlow("Tipo de corte") // Tipo de corte de papa
    val selectedCutTypePotato: StateFlow<String> get() = _selectedCutTypePotato

    // Cantidad ingresada por el usuario
    private val _selectedRice = MutableStateFlow("Cantidad")
    val selectedRice: StateFlow<String> get() = _selectedRice

    // Textura de espaguetis escogida por el usuario
    private val _selectedSpaghetti = MutableStateFlow("Textura")
    val selectedSpaghetti: StateFlow<String> get() = _selectedSpaghetti

    // Tiempo calculado
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

    fun updateSelectedTypePotato(typePotato: String) {
        _selectedTypePotato.value = typePotato
    }

    fun updateSelectedCutType(cutType: String) {
        _selectedCutTypePotato.value = cutType
    }

    fun setTimeCalculated(option: String) {
        if (_selectedTypePotato.value == "Blanca") {
            when (option) {
                "Grande" -> {
                    _timeCalculated.value = 35
                }
                "Mediana" -> {
                    _timeCalculated.value = 25
                }
                else -> {
                    _timeCalculated.value = 15
                }
            }
        } else if (_selectedTypePotato.value == "Amarilla") {
            when (option) {
                "Grande" -> {
                    _timeCalculated.value = 25
                }
                "Mediana" -> {
                    _timeCalculated.value = 20
                }
                else -> {
                    _timeCalculated.value = 10
                }
            }
        }
    }

    fun vaporTimeCalculated() {
        _timeCalculated.value = (_timeCalculated.value * 1.25).roundToInt()
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

    fun potatoCutTypeTimeCalculated(option: String) {
        if (option == "Papa en mitades") {
            _timeCalculated.value = (_timeCalculated.value * 0.75).roundToInt()
        } else if (option == "Papa en cuartos") {
            _timeCalculated.value = (_timeCalculated.value * 0.6).roundToInt()
        }
    }
}