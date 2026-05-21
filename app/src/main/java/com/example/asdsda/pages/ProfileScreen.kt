package com.example.asdsda.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asdsda.R
import com.example.asdsda.models.AuthViewModel
import com.example.asdsda.models.UsersModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit
) {

    val userEmail by authViewModel.userEmail.observeAsState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var clicked by remember { mutableStateOf(false) }

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
                text = "Профиль",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = screenWidth * 0.05f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.denis), "",
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = RoundedCornerShape(80.dp))
            )
            Column(modifier = Modifier.padding(start = 15.dp)) {
                Text(
                    text = userEmail ?: "Гость",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srb))
                )
                Text(
                    text = "Настройки",
                    fontFamily = FontFamily(Font(R.font.srr)),
                    modifier = Modifier.clickable { onItemClick("settings") },
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .padding(horizontal = screenWidth * 0.05f)
        ) {
            ProfileRow(
                "Система лояльности",
                "Чем больше вы покупаете, тем выгоднее все становится",
                R.drawable.minicard,
                0xFF1A1A1A,
                0xFF313131
            )

            ProfileRow(
                "Скидки месяца",
                "Ваши скидки в этом месяце",
                R.drawable.icons8disc,
                0xFF1B1B1B,
                0xFF242424
            )
            ProfileRow(
                "Система лояльности",
                "Чем больше вы покупаете, тем выгоднее все становится",
                R.drawable.minicard,
                0xFF2A2A2A,
                0xFF212121
            )
        }
        Spacer(Modifier.padding(top = 15.dp))
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(color = Color(0xFF313233))
                .fillMaxSize()
                .padding(horizontal = screenWidth * 0.1f)
                .padding(top = 20.dp)
        ) {
            ProfileCol(R.drawable.zakaz, "Заказы")
            ProfileCol(R.drawable.icons8help, "Тех. Поддержка")
            ProfileCol(R.drawable.fav, "Избранное")
            ProfileCol(R.drawable.uved, "Уведомление")
            ProfileCol(R.drawable.disc, "Скидки")
            ProfileCol(R.drawable.work, "Работа у нас")
            ProfileCol(R.drawable.frien, "Привести друга")
            ProfileCol(R.drawable.wat, "О сервисе")
        }
    }
}


@Composable
fun ProfileCol(
    img: Int,
    title: String
) {

    val configuration = LocalConfiguration.current
    val screenHight = configuration.screenHeightDp

    Row(
        modifier = Modifier
            .clickable {}
            .padding(top = 15.dp, bottom = 40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(img), "", modifier = Modifier.size(size = 35.dp))
        Text(
            modifier = Modifier.padding(start = 30.dp),
            text = title,
            fontFamily = FontFamily(Font(R.font.srr)),
            fontSize = (screenHight * 0.03f).sp,
            color = Color.White
        )
    }
}



@Composable
fun ProfileRow(
    title: String,
    under: String,
    img: Int,
    right: Long,
    left: Long
) {

    val configuration = LocalConfiguration.current
    val screenHight = configuration.screenHeightDp


    Box(
        Modifier.clickable {},
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(left),
                            Color(right)
                        )
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
                .height(height = 80.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(img),
                    ""
                )
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = title,
                    fontFamily = FontFamily(Font(R.font.srb)),
                    fontSize = (screenHight * 0.025f).sp,
                    color = Color.White
                )
            }
            Text(
                modifier = Modifier.padding(start = 20.dp),
                fontFamily = FontFamily(Font(R.font.srr)),
                text = under,
                fontSize = (screenHight * 0.013f).sp,
                color = Color.White
            )
        }
    }
    Spacer(modifier = Modifier.padding(top = 10.dp))
}