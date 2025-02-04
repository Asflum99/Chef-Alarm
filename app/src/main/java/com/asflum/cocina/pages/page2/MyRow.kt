package com.asflum.cocina.pages.page2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asflum.cocina.ui.theme.LightSpinachGreen
import com.asflum.cocina.ui.theme.SpinachGreen

@Composable
fun MyRow(
    text: String,
    expanded: MutableState<Boolean>,
    selected: String,
    options: List<String>,
    input: String? = null,
    viewModel: Page2ViewModel
) {
    // variables de Error
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }

    if (input == "Espaguetis" || input == "Arroz blanco") {
        isButtonEnabled = false
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val dynamicPadding = screenWidth / 30
    val dynamicHeightPadding = screenHeight / 120

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 19.sp
        )

        Spacer(modifier = Modifier.padding(dynamicPadding))

        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.Center)
        ) {
            Button(
                enabled = isButtonEnabled,
                onClick = {
                    if (text == "Tazas de agua" && (input == "" || input == ".")) {
                        showError = true
                        errorMessage = "Por favor, ingrese una cantidad"
                    }
                    expanded.value = true
                },
                modifier = Modifier.width(screenWidth / 2),
                colors = ButtonColors(Color.White, Color.DarkGray, Color.White, Color.DarkGray),
                border = BorderStroke(3.dp, SpinachGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = selected)
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
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier.background(LightSpinachGreen)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            when (text) {
                                "Alimento:" -> {
                                    viewModel.updateSelectedFood(option)
                                }

                                "Tamaño de papa:" -> {
                                    viewModel.updateSelectedPotato(option)
                                }

                                "Tazas de agua:" -> {
                                    viewModel.updateSelectedRice(option)
                                }

                                "Textura:" -> {
                                    viewModel.updateSelectedSpaghetti(option)
                                }

                                "Cocción:" -> {
                                    viewModel.updateSelectedCook(option)
                                }

                                "Tipo de papa:" -> {
                                    viewModel.updateSelectedTypePotato(option)
                                }

                                "Tipo de corte:" -> {
                                    viewModel.updateSelectedCutType(option)
                                }
                            }

                            when (viewModel.selectedFood.value) {
                                "Papa" -> {
                                    when (option) {
                                        "Grande" -> {
                                            viewModel.setTimeCalculated(option)
                                        }

                                        "Mediana" -> {
                                            viewModel.setTimeCalculated(option)
                                        }

                                        "Pequeña" -> {
                                            viewModel.setTimeCalculated(option)
                                        }

                                        "Vapor" -> {
                                            viewModel.vaporTimeCalculated()
                                        }
                                    }
                                    if (text == "Tipo de corte:") {
                                        viewModel.potatoCutTypeTimeCalculated(option)
                                    }
                                }
                                "Arroz blanco" -> {
                                    viewModel.riceTimeCalculated(option, options)
                                }
                                "Espaguetis" -> {
                                    viewModel.spaghettiTimeCalculated(option)
                                }
                            }
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
    if (text != "Alimento:") {
        Spacer(modifier = Modifier.padding(bottom = dynamicHeightPadding))
    }
}