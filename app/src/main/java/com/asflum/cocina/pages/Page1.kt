package com.asflum.cocina.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asflum.cocina.AppDatabase
import com.asflum.cocina.FoodViewModel
import com.asflum.cocina.FoodViewModelFactory
import com.asflum.cocina.MyApplication
import com.asflum.cocina.R
import com.asflum.cocina.createAlarm
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun Page1() {

    val context = LocalContext.current
    val savedConfigDao = AppDatabase.getInstance(context).savedConfigDao()

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
    LazyColumn {
        items(foodList) { food ->
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
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
                    }
                ) {
                    Text(text = food.foodName)
                }
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            MyApplication.database.savedConfigDao().delete(food.id)
                            foodViewModel.getAllFoodInfo()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.tacho_01),
                        contentDescription = "Eliminar",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}