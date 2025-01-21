package com.asflum.cocina

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.AlarmClock.ACTION_SET_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asflum.cocina.ui.theme.CocinaTheme
import kotlinx.coroutines.launch
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CocinaTheme {
                NavigationComponent()
            }
        }
    }
}

fun createAlarm(
    context: Context,
    hour: Int,
    minutes: Int,
    message: String) {
    val intent = Intent(ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_HOUR, hour)
        putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        putExtra(AlarmClock.EXTRA_MESSAGE, message)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

@Composable
fun MyRow(
    text: String,
    expanded: MutableState<Boolean>,
    selected: MutableState<String>,
    options: List<String>,
    time: MutableState<Int> = mutableIntStateOf(0)) {
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

        Spacer(modifier = Modifier.padding(5.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.Center)
        ) {
            Button(
                onClick = { expanded.value = true },
                modifier = Modifier.width(175.dp) // considerar usar valor dinámico
            ) {
                Text(text = selected.value)
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            selected.value = option
                            expanded.value = false
                            time.value = when (selected.value) {
                                "Grande" -> {
                                    30
                                }
                                "Mediana" -> {
                                    20
                                }
                                else -> {
                                    10
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun windowHeight(): Int {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val calculatePadding = screenHeight / 9
    return calculatePadding
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = windowHeight().dp)
    )
}

@Composable
fun Page1() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.TopCenter)
    ) {
        Title("Alimentos Guardados")
    }
}

@Composable
fun Page2() {

    // variables de Alimento
    val expandedFood = remember { mutableStateOf(false) }
    val selectedFood = remember { mutableStateOf("Seleccione alimento") }
    val optionsFood = listOf("Papa", "Arroz", "Fideos")

    // variables de Medición
    var expandedMeasurement by remember { mutableStateOf(false) }
    var selectedMeasurement by remember { mutableStateOf("Tipo de medición") }
    val optionsMeasurement = listOf("Gr", "Unidad", "Cup")

    // variables de Cocción
    val expandedCook = remember { mutableStateOf(false) }
    val selectedCook = remember { mutableStateOf("Tipo de cocción") }
    val optionsCook = listOf("Hervido", "Vapor")

    // variables de Papa
    val expandedPotato = remember { mutableStateOf(false) }
    val selectedPotato = remember { mutableStateOf("Tamaño de papa") }
    val optionsPotato = listOf("Grande", "Mediana", "Pequeña")
    val timeCalculated = remember { mutableIntStateOf(0) }

    var columnHeight by remember { mutableIntStateOf(0) }
    var inputNumber by remember { mutableStateOf("") }

    var calculateState by remember { mutableStateOf(false) }

    var checked by remember { mutableStateOf(false) }

    // variables de Error
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    // Tiempo
    val currentTime = LocalTime.now()

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
        .padding(16.dp)) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .onSizeChanged { size ->
                columnHeight = size.height
            }) {

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
                    value = inputNumber,
                    onValueChange = { inputNumber = it },
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

            if (selectedFood.value == "Papa") {
                MyRow(
                    text = "Tamaño de papa:",
                    expanded = expandedPotato,
                    selected = selectedPotato,
                    options = optionsPotato,
                    time = timeCalculated
                )

                Spacer(modifier = Modifier.padding(8.dp)) // considerar usar un valor dinámico
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
                        } else if (inputNumber.isEmpty()) {
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
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { timeCalculated.value -= 5 }
                    ) {
                        Text(text = "-5")
                    }
                    Text(text = "${timeCalculated.intValue} min")
                    Button(
                        onClick = { timeCalculated.value += 5 }
                    ) {
                        Text(text = "+5")
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it },
                    )
                    Button(
                        onClick = {
                            val alarmTime = currentTime.plusMinutes(timeCalculated.intValue.toLong())

                            createAlarm(
                            context = context,
                            hour = alarmTime.hour,
                            minutes = alarmTime.minute,
                            message = "Alarma")
                        }
                    ) {
                        Text(text = "Programar alarma")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationComponent() {
    val pagerState = rememberPagerState(
        pageCount = { 2 }, initialPage = 1
    )

    val coroutineScope = rememberCoroutineScope()

    val currentPage = pagerState.currentPage

    Column(modifier = Modifier.fillMaxSize()) {
        // Parte superior fija
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f) // Porcentaje de espacio para la parte superior
                .background(Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Alimentos guardados",
                    fontSize = if (currentPage == 0) 20.sp else 14.sp,
                    color = if (currentPage == 0) Color.Black else Color.Gray,
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    })

                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "Añadir alimento",
                    fontSize = if (currentPage == 1) 20.sp else 14.sp,
                    color = if (currentPage == 1) Color.Black else Color.Gray,
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    })
            }
        }
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
        ) { page ->
            when (page) {
                0 -> Page1()
                1 -> Page2()
            }
        }
    }
}