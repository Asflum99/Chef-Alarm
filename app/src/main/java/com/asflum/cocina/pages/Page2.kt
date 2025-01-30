package com.asflum.cocina.pages

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.asflum.cocina.MyApplication
import com.asflum.cocina.MyRow
import com.asflum.cocina.SavedConfig
import com.asflum.cocina.createAlarm
import com.asflum.cocina.ui.theme.DarkGray
import com.asflum.cocina.ui.theme.SpinachGreen
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun Page2(page: PagerState) {

    // Ancho de pantalla
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val dynamicWidthPadding = screenWidth / 30
    val dynamicHeightPadding = screenHeight / 120

    // variables de Alimento
    val expandedFood = remember { mutableStateOf(false) }
    val selectedFood = remember { mutableStateOf("Seleccione alimento") }
    val optionsFood = listOf("Papa", "Arroz blanco", "Espaguetis")

    // variables de Medición
    var selectedMeasurement by remember { mutableStateOf("Tipo de medición") }

    // variables de Cocción
    val expandedCook = remember { mutableStateOf(false) }
    val selectedCook = remember { mutableStateOf("Tipo de cocción") }
    val optionsCook = listOf("Hervido", "Vapor")

    // variables de Papa
    val expandedPotato = remember { mutableStateOf(false) }
    val selectedPotato = remember { mutableStateOf("Tamaño de papa") }
    val optionsPotato = listOf("Grande", "Mediana", "Pequeña")
    val sizesPotato = mapOf("Grande" to 30, "Mediana" to 20, "Pequeña" to 10)
    val cookPotato = mapOf("Hervido" to 0, "Vapor" to 5)

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
        .padding(screenWidth / 24)) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(dynamicHeightPadding)
        ) {
            item {
                MyRow(
                    text = "Alimento:",
                    expanded = expandedFood,
                    selected = selectedFood,
                    options = optionsFood
                )
            }
            item {
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

                    Spacer(modifier = Modifier.padding(dynamicWidthPadding))

                    when (selectedFood.value) {
                        "Papa" -> {
                            Button(
                                enabled = false,
                                colors = ButtonColors(
                                    Color.White,
                                    Color.DarkGray,
                                    Color.White,
                                    Color.DarkGray
                                ),
                                border = BorderStroke(3.dp, SpinachGreen),
                                onClick = {},
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Unidades")
                            }
                        }

                        "Arroz blanco" -> {
                            Button(
                                enabled = false,
                                colors = ButtonColors(
                                    Color.White,
                                    Color.DarkGray,
                                    Color.White,
                                    Color.DarkGray
                                ),
                                border = BorderStroke(3.dp, SpinachGreen),
                                onClick = {},
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Vasos")
                            }
                        }

                        "Espaguetis" -> {
                            Button(
                                enabled = false,
                                colors = ButtonColors(
                                    Color.White,
                                    Color.DarkGray,
                                    Color.White,
                                    Color.DarkGray
                                ),
                                border = BorderStroke(3.dp, SpinachGreen),
                                onClick = {},
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Gramos")
                            }
                        }

                        else -> {
                            Button(
                                enabled = false,
                                colors = ButtonColors(
                                    Color.White,
                                    Color.DarkGray,
                                    Color.White,
                                    Color.DarkGray
                                ),
                                border = BorderStroke(3.dp, SpinachGreen),
                                onClick = {},
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Tipo de medición")
                            }
                        }
                    }
                }
            }
            item {
                when (selectedFood.value) {
                    "Papa" -> {
                        MyRow(
                            "Tamaño de papa:",
                            expandedPotato,
                            selectedPotato,
                            optionsPotato,
                            timeCalculated,
                            optionsCook = selectedCook.value,
                            cookPotato = cookPotato,
                            sizesPotato = sizesPotato
                        )

                        selectedMeasurement = "Unidad"
                        foodExtra = selectedPotato.value
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
                    }
                }
            }
            item {
                when (selectedFood.value) {
                    "Papa" -> {
                        MyRow(
                            "Cocción:",
                            expandedCook,
                            selectedCook,
                            optionsCook,
                            timeCalculated,
                            cookPotato = cookPotato,
                            sizesPotato = sizesPotato
                        )
                        Spacer(modifier = Modifier.padding(dynamicHeightPadding))
                    }

                    "Espaguetis" -> {
                        MyRow(
                            text = "Cocción:",
                            expanded = expandedCook,
                            selected = selectedCook,
                            options = optionsCook,
                            input = "Espaguetis"
                        )
                        Spacer(modifier = Modifier.padding(dynamicHeightPadding))
                    }

                    "Arroz blanco" -> {
                        MyRow(
                            text = "Cocción:",
                            expanded = expandedCook,
                            selected = selectedCook,
                            options = optionsCook,
                            input = "Arroz blanco"
                        )
                        Spacer(modifier = Modifier.padding(dynamicHeightPadding))
                    }
                }
            }
            item {
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
                    modifier = Modifier.width(screenWidth / 2),
                    colors = ButtonColors(SpinachGreen, Color.White, Color.White, SpinachGreen)
                ) {
                    Text(
                        text = "Calcular",
                        color = DarkGray
                    )
                }
            }
            item {
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
            item {
                if (calculateState) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .align(Alignment.CenterHorizontally)
                    ) {
                        Button(
                            onClick = { timeCalculated.value -= 1 },
                            colors = ButtonColors(Color.White, DarkGray, Color.White, SpinachGreen),
                            border = BorderStroke(3.dp, SpinachGreen)
                        ) {
                            Text(
                                text = "-1",
                                color = DarkGray
                            )
                        }

                        Spacer(modifier = Modifier.padding(0.dp, 0.dp, screenWidth / 50, 0.dp))

                        Text(text = "${timeCalculated.intValue} min")

                        Spacer(modifier = Modifier.padding(screenWidth / 50, 0.dp, 0.dp, 0.dp))

                        Button(
                            onClick = { timeCalculated.value += 1 },
                            colors = ButtonColors(Color.White, DarkGray, Color.White, SpinachGreen),
                            border = BorderStroke(3.dp, SpinachGreen)
                        ) {
                            Text(
                                text = "+1",
                                color = DarkGray
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(end = 13.dp)
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { checked = it }
                            )
                            Text(text = "Recordar alimento")
                        }
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
                                selectedRice.value = "Cantidad"
                                selectedSpaghetti.value = "Textura"
                            },
                            colors = ButtonColors(SpinachGreen, Color.White, Color.White, SpinachGreen),
                            modifier = Modifier.width(screenWidth / 2)
                        ) {
                            Text(
                                text = "Programar alarma",
                                color = DarkGray
                            )
                        }
                    }
                }
            }
            item {
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
                        timeCalculated.intValue = 0
                    },
                    colors = ButtonColors(Color.White, Color.DarkGray, Color.White, Color.DarkGray),
                    border = BorderStroke(3.dp, SpinachGreen),
                    modifier = Modifier
                        .width(screenWidth / 2)
                ) {
                    Text(text = "Restablecer valores")
                }
            }
        }
    }
}