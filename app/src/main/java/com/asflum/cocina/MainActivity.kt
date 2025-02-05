package com.asflum.cocina

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.AlarmClock.ACTION_SET_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asflum.cocina.ui.theme.CocinaTheme
import kotlinx.coroutines.launch
import com.asflum.cocina.pages.page1.Page1
import com.asflum.cocina.pages.page2.Page2
import com.asflum.cocina.ui.theme.DarkGray
import com.asflum.cocina.ui.theme.MustardYellow
import com.asflum.cocina.ui.theme.TomatoRed
import com.asflum.cocina.ui.theme.WarmWhite

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
    message: String
) {
    val intent = Intent(ACTION_SET_ALARM).apply {
        putExtra(AlarmClock.EXTRA_HOUR, hour)
        putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        putExtra(AlarmClock.EXTRA_MESSAGE, message)
        putExtra(AlarmClock.EXTRA_SKIP_UI, true)
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
        // FALTA PONER LA VENTANA DE ERROR
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NavigationComponent() {
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )

    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage

    // Animación de transición de colores
    val text1Color by animateColorAsState(
        targetValue = if (currentPage == 0) TomatoRed else WarmWhite,
        animationSpec = tween(durationMillis = 200)
    )
    val text2Color by animateColorAsState(
        targetValue = if (currentPage == 1) TomatoRed else WarmWhite,
        animationSpec = tween(durationMillis = 200)
    )

    // Obtener altura de la barra de estado
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra de estado (para cubrir la parte superior del celular)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarHeight) // Solo cubre la barra de estado
                .background(DarkGray)
        )

        // Parte superior fija
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.18f) // Porcentaje de espacio para la parte superior
                .background(MustardYellow),
            contentAlignment = Alignment.Center // Los elementos dentro del Box (Row) estará centrado
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "Alimentos guardados",
                    fontSize = 20.sp,
                    color = text1Color,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(120.dp)
                        .clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                )
                Text(text = "Añadir alimento",
                    fontSize = 20.sp,
                    color = text2Color,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(120.dp)
                        .clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .background(WarmWhite)
        ) { page ->
            when (page) {
                0 -> Page1()
                1 -> Page2(pagerState)
            }
        }
    }
}