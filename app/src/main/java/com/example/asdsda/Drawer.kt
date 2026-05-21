package com.example.asdsda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asdsda.models.AuthViewModel

data class DrawerItem(
    val icon: Int,
    val title: String,
    val route: String
)

val drawerItems = listOf(
    DrawerItem(R.drawable.main, "Главная", "main"),
    DrawerItem(R.drawable.korzina, "Корзина", "korzina"),
    DrawerItem(R.drawable.last, "История заказов", "orders"),
    DrawerItem(R.drawable.rini, "Уведомление", "rings"),
    DrawerItem(R.drawable.phone, "Тех. поддержка", "support"),
    DrawerItem(R.drawable.sett, "Настройки", "settings")
)

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onItemClick: (String) -> Unit,
    onCloseClick: () -> Unit,
) {

    val userEmail by authViewModel.userEmail.observeAsState()

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(color = Color(0xFF121212))
            .padding(vertical = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(height = 50.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clickable { onItemClick("profile") },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = "Профиль",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Привет, ${userEmail ?: "Гость"}",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    fontSize = 18.sp
                )
            }
        }

        drawerItems.forEach { item ->
            DrawerMenuItem(
                item = item,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    item: DrawerItem,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable { onItemClick(item.route) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(item.icon),
            contentDescription = item.title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.srb)),
            fontSize = 16.sp
        )
    }
}
