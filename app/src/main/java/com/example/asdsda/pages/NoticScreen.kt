package com.example.asdsda.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asdsda.R

@Composable
fun NoticScreen(
    onBackClick: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var clicked by remember { mutableStateOf(false) }
    var EmptyNotic by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .background(Color(0xFF121212))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, bottom = 25.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(start = screenWidth * 0.1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "",
                    modifier = Modifier
                        .size(size = 25.dp)
                        .align(Alignment.CenterStart)
                        .clickable {
                            if (!clicked) {
                                clicked = true
                                onBackClick()
                            }
                        },
                    tint = Color.White
                )
            }
            Text(
                text = "Уведомления",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    if (EmptyNotic)
    {
        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Уведомлений нет",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
        }
    }
    else {
        // реализовать вид уведомлений
    }
}