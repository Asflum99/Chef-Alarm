package com.asflum.cocina.pages.page1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asflum.cocina.AppDatabase
import com.asflum.cocina.FoodViewModelFactory
import com.asflum.cocina.MyApplication
import com.asflum.cocina.R
import com.asflum.cocina.createAlarm
import com.asflum.cocina.ui.theme.DarkGray
import com.asflum.cocina.ui.theme.DarkSpinachGreen
import com.asflum.cocina.ui.theme.LightMustardYellow
import com.asflum.cocina.ui.theme.MustardYellow
import com.asflum.cocina.ui.theme.SpinachGreen
import com.asflum.cocina.ui.theme.TomatoRed
import com.asflum.cocina.ui.theme.quicksandFamily
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun Page1() {

    var isDeleteMode by remember { mutableStateOf(false) }
    val borderColor by remember { derivedStateOf { if (isDeleteMode) DarkSpinachGreen.copy(alpha = 0.5f) else DarkSpinachGreen } }

    val context = LocalContext.current
    val savedConfigDao = AppDatabase.getInstance(context).savedConfigDao()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

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
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.85f)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(foodList.size) { food ->
                    Box(
                        modifier = Modifier
//                            .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
                            .aspectRatio(1f)
                            .padding(5.dp)
                            .background(LightMustardYellow, RoundedCornerShape(8.dp))
                            .border(
                                width = 3.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                // El icono de los alimentos solo será clickable cuando isDeleteMode sea false
                                if (!isDeleteMode) {
                                    coroutineScope.launch {
                                        val currentTime = LocalTime.now()
                                        val alarmTime =
                                            currentTime.plusMinutes(foodList[food].estimatedTime.toLong())
                                        createAlarm(
                                            context,
                                            alarmTime.hour,
                                            alarmTime.minute,
                                            foodList[food].foodName
                                        )
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            when (foodList[food].foodName) {
                                "Arroz blanco" -> {
                                    Box(
                                        modifier = Modifier
                                            .weight(0.25f)
                                            .fillMaxWidth()
                                            .background(LightMustardYellow),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "${foodList[food].foodQuantity} tazas",
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = quicksandFamily
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .fillMaxWidth()
                                            .background(LightMustardYellow),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painterResource(id = R.drawable.arroz),
                                            "Taza de arroz",
                                            Modifier
                                                .size(80.dp)
                                                .padding(start = 15.dp)
                                        )
                                    }
                                    Box(
                                        Modifier
                                            .weight(0.25f)
                                            .fillMaxWidth()
                                            .background(MustardYellow),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            "${foodList[food].foodExtra} tazas de agua",
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = quicksandFamily
                                        )
                                    }
                                }

                                "Espaguetis" -> {
                                    Box(
                                        Modifier
                                            .weight(0.25f)
                                            .fillMaxWidth()
                                            .background(LightMustardYellow),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            "${foodList[food].foodQuantity} gramos",
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = quicksandFamily
                                        )
                                    }
                                    Box(
                                        Modifier
                                            .weight(0.5f)
                                            .fillMaxWidth()
                                            .background(LightMustardYellow),
                                        Alignment.Center
                                    ) {
                                        Image(
                                            painterResource(id = R.drawable.fideos),
                                            "Olla con fideos",
                                            Modifier
                                                .size(100.dp)
                                        )
                                    }
                                    Box(
                                        Modifier
                                            .weight(0.25f)
                                            .fillMaxWidth()
                                            .background(MustardYellow),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            "Textura: ${foodList[food].foodExtra}",
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = quicksandFamily
                                        )
                                    }

                                }

                                else -> {
                                    Box(
                                        Modifier
                                            .weight(0.25f)
                                            .fillMaxWidth()
                                            .background(LightMustardYellow),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            if (foodList[food].foodQuantity.toInt() > 1) {
                                                "${foodList[food].foodQuantity} papas ${foodList[food].potatoType?.lowercase()}s ${foodList[food].foodExtra?.lowercase()}s"
                                            } else {
                                                "${foodList[food].foodQuantity} papas ${foodList[food].potatoType?.lowercase()} ${foodList[food].foodExtra?.lowercase()}"
                                            },
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = quicksandFamily
                                        )
                                    }
                                    Box(
                                        Modifier
                                            .weight(0.5f)
                                            .fillMaxWidth()
                                            .background(LightMustardYellow),
                                        Alignment.Center
                                    ) {
                                        Image(
                                            painterResource(id = R.drawable.papa),
                                            "Papa",
                                            Modifier
                                                .size(80.dp)
                                        )
                                    }
                                    Box(
                                        Modifier
                                            .weight(0.25f)
                                            .fillMaxWidth()
                                            .background(MustardYellow),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            "Cocción: ${foodList[food].foodCook}",
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = quicksandFamily
                                        )
                                    }
                                }
                            }
                        }
                        if (isDeleteMode) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Gray.copy(alpha = 0.5f))
                            ) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            MyApplication.database.savedConfigDao().delete(foodList[food].id)
                                            foodViewModel.getAllFoodInfo()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(52.dp)
                                        .background(TomatoRed, CircleShape)
                                        .align(Alignment.Center)
                                ) {
                                    Image(
                                        painterResource(R.drawable.tacho_01),
                                        "Eliminar",
                                        Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .weight(0.15f)
                .background(SpinachGreen),
            Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenWidth / 50),
                modifier = Modifier.padding(screenWidth / 24)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            MyApplication.database.savedConfigDao().deleteAll()
                            foodViewModel.getAllFoodInfo()
                        }
                    },
                    colors = ButtonColors(Color.White, Color.White, Color.White, SpinachGreen),
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        "Borrar todos los alimentos",
                        color = DarkGray,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quicksandFamily
                    )
                }
                Button(
                    onClick = { isDeleteMode = !isDeleteMode },
                    colors = ButtonColors(Color.White, Color.White, Color.White, SpinachGreen),
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        if (isDeleteMode) "Cancelar" else "Borrar un alimento",
                        color = DarkGray,
                        fontWeight = FontWeight.Medium,
                        fontFamily = quicksandFamily,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()) // Obtiene la altura de la barra de navegación
                .background(DarkGray)
        )
    }
}