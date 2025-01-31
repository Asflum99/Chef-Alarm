package com.asflum.cocina.pages.page2

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asflum.cocina.MyApplication
import com.asflum.cocina.MyRow
import com.asflum.cocina.SavedConfig
import com.asflum.cocina.createAlarm
import com.asflum.cocina.ui.theme.DarkGray
import com.asflum.cocina.ui.theme.SpinachGreen
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun Page2(page: PagerState,
          viewModel: Page2ViewModel = viewModel()
) {
    var foodExtra by remember { mutableStateOf("") }

    // variables de Papa
    val expandedPotato = remember { mutableStateOf(false) }
    val selectedPotato by viewModel.selectedPotato.collectAsState()
    val optionsPotato = listOf("Grande", "Mediana", "Pequeña")
    val sizesPotato = mapOf("Grande" to 30, "Mediana" to 20, "Pequeña" to 10)

    // variables de Arroz blanco
    val expandedRice = remember { mutableStateOf(false) }
    val selectedRice by viewModel.selectedRice.collectAsState()
    val optionsRice = remember { mutableListOf<String>() }
    val multipliers = listOf(1, 1.5, 2)

    // variables de Espaguetis
    val expandedSpaghetti = remember { mutableStateOf(false) }
    val selectedSpaghetti by viewModel.selectedSpaghetti.collectAsState()
    val optionsSpaghetti = listOf("Al dente", "Suave")

    // Ancho de pantalla
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val dynamicWidthPadding = screenWidth / 30
    val dynamicHeightPadding = screenHeight / 120

    // variables de Alimento
    val expandedFood = remember { mutableStateOf(false) }
    val selectedFood by viewModel.selectedFood.collectAsState()
    val optionsFood = listOf("Papa", "Arroz blanco", "Espaguetis")

    // variables de Medición
    val selectedMeasurement by viewModel.selectedMeasurement.collectAsState()
    // Efecto que se ejecuta cuando selectedFood cambia
    LaunchedEffect(selectedFood) {
        when (selectedFood) {
            "Papa" -> {
                viewModel.updateSelectedMeasurement("Unidad")
                foodExtra = selectedPotato
            }
            "Arroz blanco" -> {
                viewModel.updateSelectedMeasurement("Vasos")
                viewModel.updateSelectedCook("Hervido")
                foodExtra = selectedRice
            }
            "Espaguetis" -> {
                viewModel.updateSelectedMeasurement("Gramos")
                viewModel.updateSelectedCook("Hervido")
                foodExtra = selectedSpaghetti
            }
        }
    }

    // variables de Cocción
    val expandedCook = remember { mutableStateOf(false) }
    val selectedCook by viewModel.selectedCook.collectAsState()
    val optionsCook = listOf("Hervido", "Vapor")

    val timeCalculated by viewModel.timeCalculated.collectAsState()

    val inputNumber = remember { mutableStateOf("") }

    var calculateState by remember { mutableStateOf(false) }

    var checked by remember { mutableStateOf(false) }

    // variables de Error
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

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
                    "Alimento:",
                    expandedFood,
                    selectedFood,
                    optionsFood,
                    viewModel = viewModel
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
                            if (newValue.isEmpty()) {
                                inputNumber.value = newValue
                            } else if (newValue.matches(Regex("^\\d*\\.?\\d*$")) && newValue.count { it == '.' } <= 1) {
                                // Verifica que el punto no sea el primer carácter
                                if (newValue.first() != '.') {
                                    inputNumber.value = newValue
                                }
                            }
                            optionsRice.clear()
                            viewModel.updateSelectedRice("Cantidad")
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

                    when (selectedFood) {
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
            when (selectedFood) {
                "Papa" -> {
                    item {
                        MyRow(
                            "Tamaño de papa:",
                            expandedPotato,
                            selectedPotato,
                            optionsPotato,
                            sizesPotato = sizesPotato,
                            viewModel = viewModel
                        )
                    }
                }
                "Arroz blanco" -> {
                    item {
                        MyRow(
                            "Tazas de agua:",
                            expandedRice,
                            selectedRice,
                            optionsRice,
                            inputNumber.value,
                            viewModel = viewModel
                        )
                    }
                }
                "Espaguetis" -> {
                    item {
                        MyRow(
                            "Textura:",
                            expandedSpaghetti,
                            selectedSpaghetti,
                            optionsSpaghetti,
                            viewModel = viewModel
                        )
                    }
                }
            }
            when (selectedFood) {
                "Papa" -> {
                    item {
                        MyRow(
                            "Cocción:",
                            expandedCook,
                            selectedCook,
                            optionsCook,
                            sizesPotato = sizesPotato,
                            viewModel = viewModel
                        )
                        Spacer(modifier = Modifier.padding(dynamicHeightPadding))
                    }
                }
                "Espaguetis" -> {
                    item {
                        MyRow(
                            text = "Cocción:",
                            expanded = expandedCook,
                            selected = selectedCook,
                            options = optionsCook,
                            input = "Espaguetis",
                            viewModel = viewModel
                        )
                        Spacer(modifier = Modifier.padding(dynamicHeightPadding))
                    }
                }
                "Arroz blanco" -> {
                    item {
                        MyRow(
                            "Cocción:",
                            expandedCook,
                            selectedCook,
                            optionsCook,
                            sizesPotato = sizesPotato,
                            viewModel = viewModel
                        )
                        Spacer(modifier = Modifier.padding(dynamicHeightPadding))
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        if (selectedFood == "Seleccione alimento") {
                            showError = true
                            errorMessage = "Por favor, seleccione un alimento"
                        } else if (inputNumber.value.isEmpty()) {
                            showError = true
                            errorMessage = "Por favor, ingrese una cantidad"
                        } else if (selectedMeasurement == "Tipo de medición") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tipo de medición"
                        } else if (selectedCook == "Tipo de cocción") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tipo de cocción"
                        } else if (selectedFood == "Papa" && selectedPotato == "Tamaño de papa") {
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
                    ) {
                        Button(
                            onClick = { viewModel.minusTimeCalculated() },
                            colors = ButtonColors(Color.White, DarkGray, Color.White, SpinachGreen),
                            border = BorderStroke(3.dp, SpinachGreen)
                        ) {
                            Text(
                                text = "-1",
                                color = DarkGray
                            )
                        }

                        Spacer(modifier = Modifier.padding(0.dp, 0.dp, screenWidth / 50, 0.dp))

                        Text(text = "$timeCalculated min")

                        Spacer(modifier = Modifier.padding(screenWidth / 50, 0.dp, 0.dp, 0.dp))

                        Button(
                            onClick = { viewModel.plusTimeCalculated() },
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
                                    currentTime.plusMinutes(timeCalculated.toLong())
                                createAlarm(
                                    context = context,
                                    hour = alarmTime.hour,
                                    minutes = alarmTime.minute,
                                    message = selectedFood
                                )

                                if (checked) {
                                    val foodNameToSave = selectedFood
                                    val foodQuantityToSave = inputNumber.value
                                    val foodMeasurementToSave = selectedMeasurement
                                    val foodCookToSave = selectedCook
                                    val foodExtraToSave = foodExtra
                                    scope.launch {
                                        val savedConfig = SavedConfig(
                                            foodName = foodNameToSave,
                                            foodQuantity = foodQuantityToSave,
                                            foodMeasurement = foodMeasurementToSave,
                                            foodCook = foodCookToSave,
                                            foodExtra = foodExtraToSave,
                                            estimatedTime = timeCalculated
                                        )
                                        MyApplication.database.savedConfigDao().insert(savedConfig)
                                    }
                                }
                                scope.launch {
                                    page.animateScrollToPage(0)
                                }
                                viewModel.updateSelectedFood("Seleccione alimento")
                                viewModel.updateSelectedMeasurement("Tipo de medición")
                                viewModel.updateSelectedCook("Tipo de cocción")
                                viewModel.updateSelectedPotato("Tamaño de papa")
                                viewModel.updateSelectedRice("Cantidad")
                                viewModel.updateSelectedSpaghetti("Textura")
                                inputNumber.value = ""
                                calculateState = false
                                checked = false
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
                        viewModel.updateSelectedFood("Seleccione alimento")
                        viewModel.updateSelectedMeasurement("Tipo de medición")
                        viewModel.updateSelectedCook("Tipo de cocción")
                        viewModel.updateSelectedPotato("Tamaño de papa")
                        viewModel.updateSelectedRice("Cantidad")
                        viewModel.updateSelectedSpaghetti("Textura")
                        inputNumber.value = ""
                        calculateState = false
                        checked = false
                        viewModel.timeCalculatedToZero()
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