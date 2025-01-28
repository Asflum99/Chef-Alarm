package com.asflum.cocina.pages

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.asflum.cocina.MyApplication
import com.asflum.cocina.MyRow
import com.asflum.cocina.SavedConfig
import com.asflum.cocina.createAlarm
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun Page2(page: PagerState) {
    // variables de Alimento
    val expandedFood = remember { mutableStateOf(false) }
    val selectedFood = remember { mutableStateOf("Seleccione alimento") }
    val optionsFood = listOf("Papa", "Arroz blanco", "Espaguetis")

    // variables de Medición
    var expandedMeasurement by remember { mutableStateOf(false) }
    var selectedMeasurement by remember { mutableStateOf("Tipo de medición") }
    val optionsMeasurement = listOf("Gr", "Unidad", "Vasos")

    // variables de Cocción
    val expandedCook = remember { mutableStateOf(false) }
    val selectedCook = remember { mutableStateOf("Tipo de cocción") }
    val optionsCook = listOf("Hervido", "Vapor")

    // variables de Papa
    val expandedPotato = remember { mutableStateOf(false) }
    val selectedPotato = remember { mutableStateOf("Tamaño de papa") }
    val optionsPotato = listOf("Grande", "Mediana", "Pequeña")

    // variables de Arroz blanco
    val expandedRice = remember { mutableStateOf(false) }
    val selectedRice = remember { mutableStateOf("Cantidad") }
    val optionsRice = remember { mutableListOf<String>() }
    val multipliers = listOf(1, 1.5, 2)

    // variables de Espaguetis
    val expandedSpaghetti = remember { mutableStateOf(false) }
    val selectedSpaghetti = remember { mutableStateOf("Textura") }
    val optionsSpaghetti = listOf("Al dente", "Suave")

    val timeCalculated = remember { mutableIntStateOf(0) }

    val inputNumber = remember { mutableStateOf("") }

    var calculateState by remember { mutableStateOf(false) }

    var checked by remember { mutableStateOf(false) }

    // variables de Error
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var foodExtra by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
        .padding(16.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            .fillMaxHeight()
            ) {

            MyRow(
                text = "Alimento",
                expanded = expandedFood,
                selected = selectedFood,
                options = optionsFood
            )

            Spacer(modifier = Modifier.padding(8.dp)) // considerar usar un valor dinámico

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = inputNumber.value,
                    onValueChange = { newValue ->
                        // Filtrar solo los caracteres numéricos
                        if (newValue.all { it.isDigit() || it == '.' } && newValue.count { it == '.' } <= 1) {
                            inputNumber.value = newValue
                        }
                        optionsRice.clear()
                        selectedRice.value = "Cantidad"
                        if (inputNumber.value != "") {
                            for (multiplier in multipliers) {
                                optionsRice.add((inputNumber.value.toDouble() * multiplier.toDouble()).toString())
                            }
                        }
                    },
                    label = { Text("Ingrese cantidad") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier.weight(1f)
                )


                Spacer(modifier = Modifier.padding(5.dp)) // considerar usar un valor dinámico

                Box {
                    Button(
                        onClick = { expandedMeasurement = true },
                        modifier = Modifier.width(175.dp) // considerar usar un valor dinámico
                    ) {
                        Text(text = selectedMeasurement)
                    }
                    DropdownMenu(expanded = expandedMeasurement,
                        onDismissRequest = { expandedMeasurement = false }) {
                        optionsMeasurement.forEach { option ->
                            DropdownMenuItem(text = { Text(text = option) }, onClick = {
                                selectedMeasurement = option
                                expandedMeasurement = false
                            })
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.padding(8.dp)) // considerar usar un valor dinámico

            MyRow(
                text = "Cocción:",
                expanded = expandedCook,
                selected = selectedCook,
                options = optionsCook
            )

            Spacer(modifier = Modifier.padding(8.dp)) // considerar usar un valor dinámico

            when (selectedFood.value) {
                "Papa" -> {
                    MyRow(
                        "Tamaño de papa:",
                        expandedPotato,
                        selectedPotato,
                        optionsPotato,
                        timeCalculated,
                        optionsCook = selectedCook.value
                    )

                    selectedMeasurement = "Unidad"
                    foodExtra = selectedPotato.value
                    Spacer(modifier = Modifier.padding(8.dp)) // considerar usar un valor dinámico
                }
                "Arroz blanco" -> {
                    MyRow(
                        "Tazas de agua:",
                        expandedRice,
                        selectedRice,
                        optionsRice,
                        timeCalculated,
                        inputNumber.value
                    )

                    selectedMeasurement = "Vasos"
                    selectedCook.value = "Hervido"
                    foodExtra = selectedRice.value
                    Spacer(modifier = Modifier.padding(8.dp)) // considerar usar un valor dinámico
                }
                "Espaguetis" -> {
                    MyRow(
                        "Textura:",
                        expandedSpaghetti,
                        selectedSpaghetti,
                        optionsSpaghetti,
                        timeCalculated
                    )

                    selectedMeasurement = "Gramos"
                    selectedCook.value = "Hervido"
                    foodExtra = selectedSpaghetti.value
                    Spacer(modifier = Modifier.padding(8.dp)) // considerar usar un valor dinámico
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        if (selectedFood.value == "Seleccione alimento") {
                            showError = true
                            errorMessage = "Por favor, seleccione un alimento"
                        } else if (inputNumber.value.isEmpty()) {
                            showError = true
                            errorMessage = "Por favor, ingrese una cantidad"
                        } else if (selectedMeasurement == "Tipo de medición") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tipo de medición"
                        } else if (selectedCook.value == "Tipo de cocción") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tipo de cocción"
                        } else if (selectedFood.value == "Papa" && selectedPotato.value == "Tamaño de papa") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tamaño de papa"
                        } else {
                            calculateState = true
                        }
                    },
                    modifier = Modifier.width(175.dp) // considerar usar un valor dinámico
                ) {
                    Text(text = "Calcular")
                }
                if (showError) {
                    AlertDialog(
                        onDismissRequest = { showError = false },
                        title = { Text("Error") },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            Button(onClick = { showError = false }) {
                                Text("Ok")
                            }
                        }
                    )
                }
            }

            if (calculateState) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { timeCalculated.value -= 1 }
                    ) {
                        Text(text = "-1")
                    }
                    Text(text = "${timeCalculated.intValue} min")
                    Button(
                        onClick = { timeCalculated.value += 1 }
                    ) {
                        Text(text = "+1")
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                    Text(text = "Recordar alimento")
                    Button(
                        onClick = {
                            val currentTime = LocalTime.now()
                            val alarmTime =
                                currentTime.plusMinutes(timeCalculated.intValue.toLong())
                            createAlarm(
                                context = context,
                                hour = alarmTime.hour,
                                minutes = alarmTime.minute,
                                message = selectedFood.value
                            )

                            if (checked) {
                                val foodNameToSave = selectedFood.value
                                val foodQuantityToSave = inputNumber.value
                                val foodMeasurementToSave = selectedMeasurement
                                val foodCookToSave = selectedCook.value
                                val foodExtraToSave = foodExtra
                                scope.launch {
                                    val savedConfig = SavedConfig(
                                        foodName = foodNameToSave,
                                        foodQuantity = foodQuantityToSave,
                                        foodMeasurement = foodMeasurementToSave,
                                        foodCook = foodCookToSave,
                                        foodExtra = foodExtraToSave,
                                        estimatedTime = timeCalculated.intValue
                                    )
                                    MyApplication.database.savedConfigDao().insert(savedConfig)
                                }
                            }
                            scope.launch {
                                page.animateScrollToPage(0)
                            }
                            selectedFood.value = "Seleccione alimento"
                            selectedMeasurement = "Tipo de medición"
                            selectedCook.value = "Tipo de cocción"
                            selectedPotato.value = "Tamaño de papa"
                            inputNumber.value = ""
                            calculateState = false
                            checked = false
                        }
                    ) {
                        Text(text = "Programar alarma")
                    }
                }
            }
            Button(
                onClick = {
                    selectedFood.value = "Seleccione alimento"
                    selectedMeasurement = "Tipo de medición"
                    selectedCook.value = "Tipo de cocción"
                    selectedPotato.value = "Tamaño de papa"
                    inputNumber.value = ""
                    calculateState = false
                    checked = false
                    selectedRice.value = "Cantidad"
                }
            ) {
                Text(text = "Restablecer valores")
            }
        }
    }
}