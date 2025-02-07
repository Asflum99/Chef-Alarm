package com.asflum.cocina.pages.page1

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asflum.cocina.database.AppDatabase
import com.asflum.cocina.database.MyApplication
import com.asflum.cocina.R
import com.asflum.cocina.createAlarm
import com.asflum.cocina.ui.theme.MediumGreen
import com.asflum.cocina.ui.theme.SoftLightOrange
import com.asflum.cocina.ui.theme.SoftOrange
import com.asflum.cocina.ui.theme.TomatoRed
import com.asflum.cocina.ui.theme.WarmWhite
import com.asflum.cocina.ui.theme.White
import com.asflum.cocina.ui.theme.quicksandFamily
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun Page1() {

    var isDeleteMode by remember { mutableStateOf(false) }
    val borderColor by remember { derivedStateOf { if (isDeleteMode) MediumGreen.copy(alpha = 0.5f) else MediumGreen } }

    val context = LocalContext.current
    val savedConfigDao = AppDatabase.getInstance(context).savedConfigDao()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val foodViewModel: FoodViewModel = viewModel(
        factory = FoodViewModelFactory(savedConfigDao)
    )

    val foodList by foodViewModel.foodList.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    var showAlert by remember { mutableStateOf(false) }

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
            // Elemento que sirve para mostrar solo los elementos visibles en la pantalla
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(foodList.size) { food ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(5.dp)
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
                                            .background(SoftLightOrange),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            if (foodList[food].foodQuantity == "1") {
                                                "${foodList[food].foodQuantity} taza"
                                            } else {
                                                "${foodList[food].foodQuantity} tazas"
                                            },
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = quicksandFamily
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .fillMaxWidth()
                                            .background(SoftLightOrange),
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
                                            .background(SoftOrange),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            if (foodList[food].foodQuantity.toDouble() < 2.0) {
                                                "${foodList[food].foodQuantity} taza de agua"
                                            } else {
                                                "${foodList[food].foodQuantity} tazas de agua"
                                            },
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = quicksandFamily
                                        )
                                    }
                                }

                                "Espaguetis" -> {
                                    Box(
                                        Modifier
                                            .weight(0.25f)
                                            .fillMaxWidth()
                                            .background(SoftLightOrange),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            "${foodList[food].foodQuantity} gramos",
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = quicksandFamily
                                        )
                                    }
                                    Box(
                                        Modifier
                                            .weight(0.5f)
                                            .fillMaxWidth()
                                            .background(SoftLightOrange),
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
                                            .background(SoftOrange),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            "Textura: ${foodList[food].foodExtra}",
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = quicksandFamily
                                        )
                                    }

                                }

                                else -> {
                                    Box(
                                        Modifier
                                            .weight(0.25f)
                                            .fillMaxWidth()
                                            .background(SoftLightOrange),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            if (foodList[food].foodQuantity.toInt() > 1) {
                                                "${foodList[food].foodQuantity} papas ${foodList[food].potatoType?.lowercase()}s ${foodList[food].foodExtra?.lowercase()}s"
                                            } else {
                                                "${foodList[food].foodQuantity} papas ${foodList[food].potatoType?.lowercase()} ${foodList[food].foodExtra?.lowercase()}"
                                            },
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = quicksandFamily,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 18.sp
                                        )
                                    }
                                    Box(
                                        Modifier
                                            .weight(0.5f)
                                            .fillMaxWidth()
                                            .background(SoftLightOrange),
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
                                            .background(SoftOrange),
                                        Alignment.Center
                                    ) {
                                        Text(
                                            when (foodList[food].potatoCut) {
                                                "Entera" -> {
                                                    "Cocción: ${foodList[food].foodCook}\nEstado: ${foodList[food].potatoCut}"
                                                }

                                                "En mitades" -> {
                                                    "Cocción: ${foodList[food].foodCook}\nEstado: ${foodList[food].potatoCut}"
                                                }

                                                else -> {
                                                    "Cocción: ${foodList[food].foodCook}\nEstado: ${foodList[food].potatoCut}"
                                                }
                                            },
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = quicksandFamily,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 18.sp
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
                                            MyApplication.database.savedConfigDao()
                                                .delete(foodList[food].id)
                                            foodViewModel.getAllFoodInfo()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(52.dp)
                                        .background(TomatoRed, CircleShape)
                                        .align(Alignment.Center)
                                        .border(3.dp, WarmWhite, CircleShape)
                                ) {
                                    Image(
                                        painterResource(R.drawable.tacho),
                                        "Eliminar",
                                        Modifier.size(26.dp),
                                        colorFilter = ColorFilter.tint(WarmWhite)
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
                .background(MediumGreen),
            Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenWidth / 50),
                modifier = Modifier.padding(screenWidth / 24)
            ) {
                Button(
                    enabled = foodList.isNotEmpty(),
                    onClick = {
                        showAlert = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        TomatoRed,
                        disabledContainerColor = TomatoRed
                    ),
                    modifier = Modifier
                        .weight(0.5f)
                ) {
                    Text(
                        "Borrar todos los alimentos",
                        color = White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = quicksandFamily
                    )
                }
                Button(
                    enabled = foodList.isNotEmpty(),
                    onClick = { isDeleteMode = !isDeleteMode },
                    colors = ButtonDefaults.buttonColors(White, TomatoRed, White, TomatoRed),
                    border = BorderStroke(3.dp, TomatoRed),
                    modifier = Modifier
                        .weight(0.5f)
                        .clip(CircleShape)
                ) {
                    Text(
                        if (isDeleteMode) "Cancelar" else "Borrar un alimento",
                        color = TomatoRed,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = quicksandFamily,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()  // Obtiene la altura de la barra de navegación
                )
                .background(Color.Black)
        )
    }
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = {
                Text(
                    "Alerta",
                    color = Color.Black
                )
            },
            text = {
                Text(
                    "¿Seguro que quieres eliminar todos los alimentos guardados? Esta acción es irreversible",
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAlert = false
                        coroutineScope.launch {
                            MyApplication.database.savedConfigDao().deleteAll()
                            foodViewModel.getAllFoodInfo()
                        }
                    },
                    border = BorderStroke(1.dp, TomatoRed),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White
                    )
                ) {
                    Text(
                        "Sí",
                        color = TomatoRed
                    )
                }
                Button(
                    onClick = { showAlert = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediumGreen
                    )
                ) {
                    Text(
                        "No",
                        color = White
                    )
                }
            },
            containerColor = White
        )
    }
}