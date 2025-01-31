package com.asflum.cocina.pages.page1

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asflum.cocina.AppDatabase
import com.asflum.cocina.FoodViewModelFactory
import com.asflum.cocina.MyApplication
import com.asflum.cocina.R
import com.asflum.cocina.createAlarm
import com.asflum.cocina.ui.theme.DarkGray
import com.asflum.cocina.ui.theme.SpinachGreen
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun Page1() {

    val context = LocalContext.current
    val savedConfigDao = AppDatabase.getInstance(context).savedConfigDao()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val dynamicHeightPadding = screenHeight / 120

    val foodViewModel: FoodViewModel = viewModel(
        factory = FoodViewModelFactory(savedConfigDao)
    )

    val foodList by foodViewModel.foodList.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // Corrutina para conseguir la info de los alimentos
    LaunchedEffect(Unit) {
        foodViewModel.getAllFoodInfo()
    }

    // Mostrar la columna con cada alimento
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenWidth / 24)
        ) {
            items(foodList) { food ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val currentTime = LocalTime.now()
                                val alarmTime =
                                    currentTime.plusMinutes(food.estimatedTime.toLong())
                                createAlarm(
                                    context = context,
                                    hour = alarmTime.hour,
                                    minutes = alarmTime.minute,
                                    message = food.foodName
                                )
                            }
                        },
                        colors = ButtonColors(Color.White, Color.DarkGray, Color.White, Color.DarkGray),
                        border = BorderStroke(3.dp, SpinachGreen),
                        modifier = Modifier.width(screenWidth / 1.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        when (food.foodName) {
                            "Arroz blanco" -> {
                                Text(text = "${food.foodQuantity} tazas de arroz blanco con ${food.foodExtra} tazas de agua")
                            }
                            "Espaguetis" -> {
                                Text(text = "${food.foodQuantity} gramos de espaguetis de textura ${food.foodExtra?.lowercase()}")
                            }
                            else -> {
                                Text(text = "${food.foodQuantity} papas de tamaño ${food.foodExtra?.lowercase()}. Cocción: ${food.foodCook}")
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    MyApplication.database.savedConfigDao().delete(food.id)
                                    foodViewModel.getAllFoodInfo()
                                }
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.tacho_01),
                                contentDescription = "Eliminar",
                                tint = Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(dynamicHeightPadding))
            }
            item {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            MyApplication.database.savedConfigDao().deleteAll()
                            foodViewModel.getAllFoodInfo()
                        }
                    },
                    modifier = Modifier.width(screenWidth / 2),
                    colors = ButtonColors(SpinachGreen, Color.White, Color.White, SpinachGreen)
                ) {
                    Text(
                        text = "Borrar todo",
                        color = DarkGray
                    )
                }
            }
        }
    }
}