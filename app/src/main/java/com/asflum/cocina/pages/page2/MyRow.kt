package com.asflum.cocina.pages.page2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asflum.cocina.ui.theme.DarkGray
import com.asflum.cocina.ui.theme.DarkSpinachGreen
import com.asflum.cocina.ui.theme.LightSpinachGreen
import com.asflum.cocina.ui.theme.SpinachGreen
import com.asflum.cocina.ui.theme.quicksandFamily

@Composable
fun MyRow(
    text: String,
    expanded: MutableState<Boolean>,
    selected: String,
    options: List<String>,
    viewModel: Page2ViewModel
) {
    // variables de Error
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }

    val input by viewModel.inputNumber.collectAsState()

    val selectedFood by viewModel.selectedFood.collectAsState()
    if ((text == "Cocción:" && selectedFood == "Espaguetis") || (text == "Cocción:" && selectedFood == "Arroz blanco")) {
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
            text,
            Modifier.weight(1f),
            fontSize = 19.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = quicksandFamily,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(dynamicPadding))

        Box(
            Modifier
                .weight(1f)
                .wrapContentSize(Alignment.Center)
        ) {
            Button(
                enabled = isButtonEnabled,
                onClick = {
                    if (text == "Tazas de agua:" && (input == "" || input == ".")) {
                        showError = true
                        errorMessage = "Por favor, ingrese una cantidad"
                    }
                    expanded.value = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(Color.White, Color.DarkGray, Color.White, Color.DarkGray),
                border = BorderStroke(3.dp, SpinachGreen),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    selected,
                    fontWeight = FontWeight.Medium,
                    fontFamily = quicksandFamily
                )
            }
            if (showError) {
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
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier.background(LightSpinachGreen)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                color = DarkGray,
                                fontWeight = FontWeight.Medium,
                                fontFamily = quicksandFamily
                            )
                        },
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