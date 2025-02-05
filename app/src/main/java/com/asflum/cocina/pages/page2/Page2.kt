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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asflum.cocina.MyApplication
import com.asflum.cocina.SavedConfig
import com.asflum.cocina.createAlarm
import com.asflum.cocina.ui.theme.DarkGray
import com.asflum.cocina.ui.theme.DarkSpinachGreen
import com.asflum.cocina.ui.theme.LightSpinachGreen
import com.asflum.cocina.ui.theme.SpinachGreen
import com.asflum.cocina.ui.theme.quicksandFamily
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun Page2(
    page: PagerState,
    viewModel: Page2ViewModel = viewModel()
) {
    // variables de Papa
    val expandedPotato = remember { mutableStateOf(false) }
    val selectedPotato by viewModel.selectedPotato.collectAsState()
    val optionsPotato = listOf("Grande", "Mediana", "Pequeña")
    val expandedTypePotato = remember { mutableStateOf(false) }
    val selectedTypePotato by viewModel.selectedTypePotato.collectAsState()
    val optionsTypePotato = listOf("Blanca", "Amarilla")
    val expandedCutTypePotato = remember { mutableStateOf(false) }
    val selectedCutTypePotato by viewModel.selectedCutTypePotato.collectAsState()
    val optionsCutTypePotato = listOf("Papa entera", "Papa en mitades", "Papa en cuartos")

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
            }

            "Arroz blanco" -> {
                viewModel.updateSelectedMeasurement("Vasos")
                viewModel.updateSelectedCook("Hervido")
            }

            "Espaguetis" -> {
                viewModel.updateSelectedMeasurement("Gramos")
                viewModel.updateSelectedCook("Hervido")
            }
        }
    }

    // variables de Cocción
    val expandedCook = remember { mutableStateOf(false) }
    val selectedCook by viewModel.selectedCook.collectAsState()
    val optionsCook = listOf("Hervido", "Vapor")

    val timeCalculated by viewModel.timeCalculated.collectAsState()

    val inputNumber by viewModel.inputNumber.collectAsState()

    var calculateState by remember { mutableStateOf(false) }

    var checked by remember { mutableStateOf(false) }

    // variables de Error
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

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
                    viewModel
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dynamicWidthPadding * 2),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = inputNumber,
                        onValueChange = { newValue ->
                            // Filtrar solo los caracteres numéricos
                            if (newValue.isEmpty()) {
                                viewModel.updateInputNumber(newValue)
                            } else if (newValue.matches(Regex("^\\d*\\.?\\d*$")) && newValue.count { it == '.' } <= 1) {
                                // Verifica que el punto no sea el primer carácter
                                if (newValue.first() != '.') {
                                    viewModel.updateInputNumber(newValue)
                                }
                            }
                            optionsRice.clear()
                            viewModel.updateSelectedRice("Cantidad")
                            if (newValue != "" && newValue != ".") {
                                for (multiplier in multipliers) {
                                    optionsRice.add((newValue.toDouble() * multiplier.toDouble()).toString())
                                }
                            }
                        },
                        textStyle = TextStyle(color = Color.Black),
                        label = {
                            Text(
                                "Ingrese cantidad",
                                color = DarkGray,
                                fontWeight = FontWeight.Medium,
                                fontFamily = quicksandFamily
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        modifier = Modifier
                            .weight(0.5f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SpinachGreen,
                            focusedLabelColor = DarkSpinachGreen,
                            cursorColor = Color.Black,
                        )
                    )

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
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Unidades",
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = quicksandFamily
                                )
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
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Vasos",
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = quicksandFamily
                                )
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
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Gramos",
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = quicksandFamily
                                )
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
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Tipo de medición",
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = quicksandFamily
                                )
                            }
                        }
                    }
                }
            }
            when (selectedFood) {
                "Papa" -> {
                    item {
                        MyRow(
                            "Tipo de papa:",
                            expandedTypePotato,
                            selectedTypePotato,
                            optionsTypePotato,
                            viewModel
                        )
                    }
                    item {
                        MyRow(
                            "Tamaño de papa:",
                            expandedPotato,
                            selectedPotato,
                            optionsPotato,
                            viewModel
                        )
                    }
                    item {
                        MyRow(
                            "Tipo de corte:",
                            expandedCutTypePotato,
                            selectedCutTypePotato,
                            optionsCutTypePotato,
                            viewModel
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
                            viewModel
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
                            viewModel
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
                            viewModel
                        )
                    }
                }

                "Espaguetis" -> {
                    item {
                        MyRow(
                            "Cocción:",
                            expandedCook,
                            selectedCook,
                            optionsCook,
                            viewModel
                        )
                    }
                }

                "Arroz blanco" -> {
                    item {
                        MyRow(
                            "Cocción:",
                            expandedCook,
                            selectedCook,
                            optionsCook,
                            viewModel
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        if (selectedFood == "Seleccione alimento") {
                            showError = true
                            errorMessage = "Por favor, seleccione un alimento"
                        } else if (inputNumber.isEmpty()) {
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
                        } else if (selectedFood == "Papa" && selectedTypePotato == "Tipo de papa") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tipo de papa"
                        } else if (selectedFood == "Papa" && selectedCutTypePotato == "Tipo de corte") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tipo de corte para la papa"
                        } else {
                            calculateState = true
                        }
                    },
                    modifier = Modifier.width(screenWidth / 2),
                    colors = ButtonColors(SpinachGreen, Color.White, Color.White, SpinachGreen)
                ) {
                    Text(
                        "Calcular",
                        color = DarkGray,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quicksandFamily
                    )
                }
            }
            if (calculateState) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(screenWidth / 50)
                    ) {
                        Button(
                            onClick = { viewModel.minusTimeCalculated() },
                            colors = ButtonColors(Color.White, DarkGray, Color.White, SpinachGreen),
                            border = BorderStroke(3.dp, SpinachGreen)
                        ) {
                            Text(
                                "-1",
                                color = DarkGray
                            )
                        }

                        Text("$timeCalculated min")

                        Button(
                            onClick = { viewModel.plusTimeCalculated() },
                            colors = ButtonColors(Color.White, DarkGray, Color.White, SpinachGreen),
                            border = BorderStroke(3.dp, SpinachGreen)
                        ) {
                            Text(
                                "+1",
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
                                onCheckedChange = { checked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = SpinachGreen
                                )
                            )
                            Text("Recordar alimento")
                        }
                        Button(
                            onClick = {
                                val currentTime = LocalTime.now()
                                val alarmTime =
                                    currentTime.plusMinutes(timeCalculated.toLong())
                                createAlarm(
                                    context,
                                    alarmTime.hour,
                                    alarmTime.minute,
                                    selectedFood
                                )

                                if (checked) {
                                    val foodNameToSave = selectedFood
                                    val foodQuantityToSave = inputNumber
                                    val foodMeasurementToSave = selectedMeasurement
                                    val foodCookToSave = selectedCook
                                    val foodExtraToSave: String = when (selectedFood) {
                                        "Papa" -> {
                                            viewModel.selectedPotato.value
                                        }

                                        "Arroz blanco" -> {
                                            viewModel.selectedRice.value
                                        }

                                        else -> {
                                            viewModel.selectedSpaghetti.value
                                        }
                                    }
                                    val potatoTypeToSave = selectedTypePotato
                                    val potatoCutTypeToSave = selectedCutTypePotato
                                    coroutineScope.launch {
                                        val savedConfig = SavedConfig(
                                            foodName = foodNameToSave,
                                            foodQuantity = foodQuantityToSave,
                                            foodMeasurement = foodMeasurementToSave,
                                            foodCook = foodCookToSave,
                                            foodExtra = foodExtraToSave,
                                            estimatedTime = timeCalculated,
                                            potatoType = potatoTypeToSave,
                                            potatoCut = potatoCutTypeToSave
                                        )
                                        MyApplication.database.savedConfigDao().insert(savedConfig)
                                    }
                                }
                                coroutineScope.launch {
                                    page.animateScrollToPage(0)
                                }
                                viewModel.updateSelectedFood("Seleccione alimento")
                                viewModel.updateSelectedMeasurement("Tipo de medición")
                                viewModel.updateSelectedCook("Tipo de cocción")
                                viewModel.updateSelectedPotato("Tamaño de papa")
                                viewModel.updateSelectedTypePotato("Tipo de papa")
                                viewModel.updateSelectedCutType("Tipo de corte")
                                viewModel.updateSelectedRice("Cantidad")
                                viewModel.updateSelectedSpaghetti("Textura")
                                viewModel.updateInputNumber("")
                                calculateState = false
                                checked = false
                            },
                            colors = ButtonColors(
                                SpinachGreen,
                                Color.White,
                                Color.White,
                                SpinachGreen
                            ),
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
                Spacer(modifier = Modifier.padding(bottom = dynamicHeightPadding))
                Button(
                    onClick = {
                        viewModel.updateSelectedFood("Seleccione alimento")
                        viewModel.updateSelectedMeasurement("Tipo de medición")
                        viewModel.updateSelectedCook("Tipo de cocción")
                        viewModel.updateSelectedPotato("Tamaño de papa")
                        viewModel.updateSelectedTypePotato("Tipo de papa")
                        viewModel.updateSelectedCutType("Tipo de corte")
                        viewModel.updateSelectedRice("Cantidad")
                        viewModel.updateSelectedSpaghetti("Textura")
                        viewModel.updateInputNumber("")
                        calculateState = false
                        checked = false
                        viewModel.timeCalculatedToZero()
                    },
                    colors = ButtonColors(Color.White, Color.DarkGray, Color.White, Color.DarkGray),
                    border = BorderStroke(3.dp, SpinachGreen),
                    modifier = Modifier
                        .width(screenWidth / 2)
                ) {
                    Text(
                        "Restablecer valores",
                        fontWeight = FontWeight.Medium,
                        fontFamily = quicksandFamily
                    )
                }
            }
            if (showError) {
                item {
                    AlertDialog(
                        onDismissRequest = { showError = false },
                        title = { Text("Error") },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            Button(
                                onClick = { showError = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkSpinachGreen
                                )
                            ) {
                                Text("Ok")
                            }
                        },
                        containerColor = LightSpinachGreen
                    )
                }
            }
        }
    }
}