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
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.asflum.cocina.database.MyApplication
import com.asflum.cocina.database.SavedConfig
import com.asflum.cocina.createAlarm
import com.asflum.cocina.ui.theme.DarkGreen
import com.asflum.cocina.ui.theme.LightGray
import com.asflum.cocina.ui.theme.MediumGreen
import com.asflum.cocina.ui.theme.White
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
    val optionsCutTypePotato = listOf("Entera", "En mitades", "En cuartos")

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
                viewModel.updateSelectedCook("Elegir")
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

    val customSelectionColors = TextSelectionColors(
        handleColor = MediumGreen, // Color del "handle"
        backgroundColor = LocalTextSelectionColors.current.backgroundColor // Mantiene el color por defecto
    )

    val buttonCalculateColor by remember { derivedStateOf { if (calculateState) Color.White else DarkGreen } }
    val buttonCalculateBorderWidth by remember { derivedStateOf { if (calculateState) 3.dp else 0.dp } }
    val buttonCalculateBorderColor by remember { derivedStateOf { if (calculateState) MediumGreen else Color.Transparent } }
    val buttonCalculateTextColor by remember { derivedStateOf { if (calculateState) Color.Black else White } }

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
                    CompositionLocalProvider(LocalTextSelectionColors provides customSelectionColors) {
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
                                viewModel.updateSelectedRice("Elegir")
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
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold,
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
                                focusedBorderColor = MediumGreen,
                                cursorColor = Color.Black,
                            )
                        )
                    }

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
                                border = BorderStroke(3.dp, LightGray),
                                onClick = {},
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Unidades",
                                    fontWeight = FontWeight.SemiBold,
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
                                border = BorderStroke(3.dp, LightGray),
                                onClick = {},
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Vasos",
                                    fontWeight = FontWeight.SemiBold,
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
                                border = BorderStroke(3.dp, LightGray),
                                onClick = {},
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Gramos",
                                    fontWeight = FontWeight.SemiBold,
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
                                border = BorderStroke(3.dp, LightGray),
                                onClick = {},
                                modifier = Modifier.weight(0.5f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Tipo de medición",
                                    fontWeight = FontWeight.SemiBold,
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
                            "Estado de la papa:",
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
                            "Tipo de cocción:",
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
                            "Tipo de cocción:",
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
                            "Tipo de cocción:",
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
                        if (selectedFood == "Elegir") {
                            showError = true
                            errorMessage = "Por favor, seleccione un alimento"
                        } else if (inputNumber.isEmpty()) {
                            showError = true
                            errorMessage = "Por favor, ingrese una cantidad"
                        } else if (selectedFood == "Papa" && selectedTypePotato == "Elegir") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tipo de papa"
                        } else if (selectedFood == "Papa" && selectedPotato == "Elegir") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tamaño de papa"
                        } else if (selectedFood == "Papa" && selectedCutTypePotato == "Elegir") {
                            showError = true
                            errorMessage = "Por favor, seleccione el estado de la papa"
                        } else if (selectedFood == "Arroz blanco" && selectedRice == "Elegir") {
                            showError = true
                            errorMessage = "Por favor, seleccione una cantidad de tazas de agua"
                        } else if (selectedCook == "Elegir") {
                            showError = true
                            errorMessage = "Por favor, seleccione un tipo de cocción"
                        } else if (selectedFood == "Espaguetis" && selectedSpaghetti == "Elegir") {
                            showError = true
                            errorMessage = "Por favor, seleccione una textura"
                        } else {
                            calculateState = true
                        }
                    },
                    modifier = Modifier.width(screenWidth / 2),
                    colors = ButtonColors(
                        buttonCalculateColor,
                        Color.White,
                        Color.White,
                        Color.White
                    ),
                    border = BorderStroke(buttonCalculateBorderWidth, buttonCalculateBorderColor)
                ) {
                    Text(
                        "Calcular",
                        color = buttonCalculateTextColor,
                        fontWeight = FontWeight.SemiBold,
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
                            colors = ButtonColors(
                                MediumGreen,
                                Color.Black,
                                Color.White,
                                Color.White
                            ),
                            border = BorderStroke(0.dp, Color.Transparent)
                        ) {
                            Text(
                                "-1",
                                color = White,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = quicksandFamily
                            )
                        }

                        Text("$timeCalculated min")

                        Button(
                            onClick = { viewModel.plusTimeCalculated() },
                            colors = ButtonColors(
                                MediumGreen,
                                Color.Black,
                                Color.White,
                                Color.White
                            ),
                            border = BorderStroke(3.dp, MediumGreen)
                        ) {
                            Text(
                                "+1",
                                color = White,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = quicksandFamily
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
                                    checkedColor = MediumGreen,
                                    uncheckedColor = LightGray,
                                    checkmarkColor = White
                                )
                            )
                            Text(
                                "Recordar alimento",
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = quicksandFamily
                            )
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
                                viewModel.updateSelectedFood("Elegir")
                                viewModel.updateSelectedMeasurement("Tipo de medición")
                                viewModel.updateSelectedCook("Elegir")
                                viewModel.updateSelectedPotato("Elegir")
                                viewModel.updateSelectedTypePotato("Elegir")
                                viewModel.updateSelectedCutType("Elegir")
                                viewModel.updateSelectedRice("Cantidad")
                                viewModel.updateSelectedSpaghetti("Elegir")
                                viewModel.updateInputNumber("")
                                optionsRice.clear()
                                calculateState = false
                                checked = false
                            },
                            colors = ButtonColors(
                                DarkGreen,
                                Color.White,
                                Color.White,
                                Color.White
                            ),
                            modifier = Modifier.width(screenWidth / 2)
                        ) {
                            Text(
                                text = "Programar alarma",
                                color = White,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = quicksandFamily
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = dynamicHeightPadding))
                Button(
                    onClick = {
                        viewModel.updateSelectedFood("Elegir")
                        viewModel.updateSelectedMeasurement("Tipo de medición")
                        viewModel.updateSelectedCook("Elegir")
                        viewModel.updateSelectedPotato("Elegir")
                        viewModel.updateSelectedTypePotato("Elegir")
                        viewModel.updateSelectedCutType("Elegir")
                        viewModel.updateSelectedRice("Elegir")
                        viewModel.updateSelectedSpaghetti("Elegir")
                        viewModel.updateInputNumber("")
                        optionsRice.clear()
                        calculateState = false
                        checked = false
                        viewModel.timeCalculatedToZero()
                    },
                    colors = ButtonColors(Color.White, Color.DarkGray, Color.White, Color.DarkGray),
                    border = BorderStroke(3.dp, MediumGreen),
                    modifier = Modifier
                        .width(screenWidth / 2)
                ) {
                    Text(
                        "Restablecer valores",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = quicksandFamily
                    )
                }
            }
            if (showError) {
                item {
                    AlertDialog(
                        onDismissRequest = { showError = false },
                        title = {
                            Text(
                                "Error",
                                color = Color.Black
                            )
                        },
                        text = {
                            Text(
                                errorMessage,
                                color = Color.Black
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = { showError = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MediumGreen
                                )
                            ) {
                                Text(
                                    "Ok",
                                    color = White
                                )
                            }
                        },
                        containerColor = White
                    )
                }
            }
        }
    }
}