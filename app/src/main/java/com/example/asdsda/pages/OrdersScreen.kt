package com.example.asdsda.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asdsda.R

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OrdersScreen(onBackClick: () -> Unit) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var clicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(start = screenWidth * 0.1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "",
                    modifier = Modifier
                        .size(size = 30.dp)
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
                text = "Заказы",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        val title = listOf("")

        LazyColumn(
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            items(
                title,
            )
            { title ->
                OrderItem(
                    "ROSTIC'S",
                    "741 ₽",
                    "12 сентября 14:25, 2024",
                    "Доставлен",
                    Color.Green,
                    R.drawable.roll,
                    R.drawable.roll
                )
                OrderItem("ROSTIC'S", "238 ₽", "23 сентября 11:34, 2024", "Отменен", Color.Red)
                OrderItem(
                    "ROSTIC'S",
                    "1541 ₽",
                    "28 сентября 18:12, 2024",
                    "Доставлен",
                    Color.Green,
                    R.drawable.shef,
                    R.drawable.rost,
                    R.drawable.boxi,
                    R.drawable.roll
                )
            }
        }
    }
}

@Composable
fun OrderItem(
    orderTitle: String,
    orderCost: String,
    orderData: String,
    orderStatus: String,
    txtcolor: Color,
    img1: Int? = null,
    img2: Int? = null,
    img3: Int? = null,
    img4: Int? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()

            .padding(vertical = 8.dp)
            .clip(shape = RoundedCornerShape(25.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 0.7f)
                    .padding(16.dp)
            ) {
                Text(
                    text = orderTitle,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.srb)),
                    fontSize = 24.sp
                )
                Text(
                    text = orderData,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.srm)),
                    fontSize = 16.sp
                )

                Text(
                    text = orderCost,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.srm)),
                    fontSize = 36.sp
                )
                Text(
                    text = orderStatus,
                    color = txtcolor,
                    fontFamily = FontFamily(Font(R.font.srm)),
                    fontSize = 16.sp
                )
            }
            Column(
                modifier = Modifier
                    .weight(weight = 0.5f)
                    .padding(top = 15.dp)
            ) {
                Row {
                    if (img1 != null) {
                        Image(
                            painter = painterResource(img1),
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .size(width = 60.dp, height = 60.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(width = 60.dp, height = 0.dp)
                        )
                    }
                    if (img2 != null) {
                        Image(
                            painter = painterResource(img2),
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .size(width = 60.dp, height = 60.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(width = 60.dp, height = 0.dp)
                        )
                    }
                }
                Row {
                    if (img3 != null) {
                        Image(
                            painter = painterResource(img3),
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .size(width = 60.dp, height = 60.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(width = 60.dp, height = 0.dp)
                        )
                    }
                    if (img4 != null) {
                        Image(
                            painter = painterResource(img4),
                            contentDescription = "",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .size(width = 60.dp, height = 60.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(width = 60.dp, height = 0.dp)
                        )
                    }
                }
            }
        }
    }
}