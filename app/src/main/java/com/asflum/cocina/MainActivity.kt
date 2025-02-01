package com.asflum.cocina

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.AlarmClock.ACTION_SET_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asflum.cocina.ui.theme.CocinaTheme
import kotlinx.coroutines.launch
import com.asflum.cocina.pages.page1.Page1
import com.asflum.cocina.pages.page2.Page2
import com.asflum.cocina.ui.theme.MustardYellow
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

    Column(modifier = Modifier.fillMaxSize()) {
        // Parte superior fija
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Porcentaje de espacio para la parte superior
                .weight(0.2f)
                .background(MustardYellow)
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