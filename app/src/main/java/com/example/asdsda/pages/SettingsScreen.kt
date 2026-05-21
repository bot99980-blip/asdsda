package com.example.asdsda.pages

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.asdsda.R
import com.example.asdsda.models.AuthViewModel
import com.example.asdsda.models.UsersModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit
) {

    var selectedTab by remember { mutableStateOf(SettingsTab.PROFILE) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var clicked by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Настройки",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        SettingsTabs(
            selectedTab = selectedTab,
            onTabSelected = { tab -> selectedTab = tab },
            modifier = Modifier.fillMaxWidth()
        )

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                ) togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) { targetTab ->
            when (targetTab) {
                SettingsTab.PROFILE -> ProfileContent(authViewModel = authViewModel)
                SettingsTab.PAYMENT -> PaymentContent()
                SettingsTab.NOTIFICATIONS -> NotificationsContent()
            }
        }
    }
}

enum class SettingsTab {
    PROFILE, PAYMENT, NOTIFICATIONS
}

data class SettingsTabItem(
    val tab: SettingsTab,
    val title: String,
    val icon: Int? = null
)

@Composable
fun SettingsTabs(
    selectedTab: SettingsTab,
    onTabSelected: (SettingsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = remember {
        listOf(
            SettingsTabItem(SettingsTab.PROFILE, "Профиль"),
            SettingsTabItem(SettingsTab.PAYMENT, "Способы оплаты"),
            SettingsTabItem(SettingsTab.NOTIFICATIONS, "Уведомления")
        )
    }

    val indicatorPositions = remember(tabs) {
        tabs.map { it.tab }
    }

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = indicatorPositions.indexOf(selectedTab),
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabIndicator(
                    tabPositions = tabPositions,
                    selectedTabIndex = indicatorPositions.indexOf(selectedTab)
                )
            },
            divider = {}
        ) {
            tabs.forEach { tabItem ->
                Tab(
                    modifier = Modifier.background(color = Color(0xFF121212)),
                    selected = tabItem.tab == selectedTab,
                    onClick = { onTabSelected(tabItem.tab) },
                    text = {
                        Text(
                            text = tabItem.title,
                            color = if (tabItem.tab == selectedTab) Color.White else Color(
                                0xFFAAAAAA
                            ),
                            fontSize = 13.sp,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TabIndicator(
    tabPositions: List<TabPosition>,
    selectedTabIndex: Int
) {
    AnimatedContent(
        targetState = selectedTabIndex,
        transitionSpec = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) togetherWith slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(0)
            )
        }
    ) { targetIndex ->
        Box(
            modifier = Modifier
                .tabIndicatorOffset(tabPositions[targetIndex])
                .fillMaxSize()
                .border(width = 1.dp, color = Color.White)
                .background(
                    color = Color(0x00000000)
                )
        )
    }
}

private fun Modifier.tabIndicatorOffset(
    currentTabPosition: TabPosition
): Modifier = composed(
    factory = {
        val currentTabWidth = currentTabPosition.width
        val indicatorOffset = currentTabPosition.left

        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorOffset)
            .width(currentTabWidth)
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    authViewModel: AuthViewModel
) {

    val usersModel = remember { mutableStateOf(UsersModel()) }
    val isLoading = remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val listener: ListenerRegistration? = if (currentUser != null) {
            Firebase.firestore.collection("users")
                .document(currentUser?.uid!!)
                .addSnapshotListener { snapshot, error ->
                    isLoading.value = false
                    if (error != null) {
                        println("Ошибка при загрузке корзины: ${error.message}")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val result = snapshot.toObject(UsersModel::class.java)
                        if (result != null) {
                            usersModel.value = result
                        }
                    } else {
                        usersModel.value = UsersModel()
                    }
                }
        } else {
            isLoading.value = false
            null
        }

        onDispose {
            listener?.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Информация профиля",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        var mail by remember { mutableStateOf("") }
        var login by remember { mutableStateOf("") }
        var number by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Логин",
                color = Color(0xFFAAAAAA),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.srr))
            )
            TextField(
                value = login,
                onValueChange = { login = it },
                label = {
                    Text("")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Text(
                text = "Маил",
                color = Color(0xFFAAAAAA),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.srr))
            )
            TextField(
                value = usersModel.value.email,
                onValueChange = { mail = it },
                label = {
                    Text("")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Text(
                text = "Номер телефона",
                color = Color(0xFFAAAAAA),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.srr))
            )
            TextField(
                value = number,
                onValueChange = { number = it },
                label = {
                    Text("")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }


        Spacer(modifier = Modifier.height(34.dp))

        OutlinedButton(
            onClick = {
                authViewModel.signout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFFFFFFF)
            ),
            border = BorderStroke(1.dp, Color(0xFFFFFFFF))
        ) {
            Text("Выйти из аккаунта")
        }
    }
}


@Composable
fun PaymentContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Способы оплаты",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        PaymentMethod("•••• 4512", "Visa", R.drawable.cards)
        PaymentMethod("•••• 7812", "MasterCard", R.drawable.cards)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {  },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFFFFFFF)
            ),
            border = BorderStroke(1.dp, Color(0xFFFFFFFF))
        ) {
            Text("Добавить карту")
        }
    }
}


@Composable
fun NotificationsContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Настройки уведомлений",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        NotificationSwitch(
            title = "Push-уведомления",
            subtitle = "Получать уведомления о заказах",
            checked = true
        )

        NotificationSwitch(
            title = "Email-уведомления",
            subtitle = "Получать уведомления на почту",
            checked = true
        )

        NotificationSwitch(
            title = "SMS-уведомления",
            subtitle = "Получать SMS о статусе заказа",
            checked = false
        )

        NotificationSwitch(
            title = "Рекламные рассылки",
            subtitle = "Получать информацию об акциях",
            checked = false
        )
    }
}


@Composable
private fun PaymentMethod(cardNumber: String, cardType: String, iconRes: Int) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = cardType,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = cardType,
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = cardNumber,
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp
            )
        }
        Icon(
            painter = painterResource(R.drawable.select),
            contentDescription = "Действия",
            tint = Color(0xFFAAAAAA),
            modifier = Modifier.clickable { showDialog = true }
        )
    }
    Divider(color = Color(0xFF333333), thickness = 1.dp)

    if (showDialog) {
        PaymentMethodDialog(
            cardNumber = cardNumber,
            cardType = cardType,
            onDismiss = { showDialog = false },
            onEdit = {
                showDialog = false
            },
            onDelete = {
                showDialog = false
            }
        )
    }
}

@Composable
fun PaymentMethodDialog(
    cardNumber: String,
    cardType: String,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Действия с картой",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    modifier = Modifier.padding(bottom = 8.dp)
                )


                Text(
                    text = "$cardType $cardNumber",
                    color = Color(0xFFAAAAAA),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEdit() }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.select),
                        contentDescription = "Редактировать",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Редактировать карту",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                Divider(color = Color(0xFF333333), thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDelete() }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.cross),
                        contentDescription = "Удалить",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Удалить карту",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отмена", color = Color(0xFFAAAAAA))
                }
            }
        }
    }
}

@Composable
private fun NotificationSwitch(title: String, subtitle: String, checked: Boolean) {
    var isChecked by remember { mutableStateOf(checked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = subtitle,
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFFFFFFF),
                checkedTrackColor = Color(0xFFA5A5A5).copy(alpha = 0.5f),
                uncheckedThumbColor = Color(0xFF666666),
                uncheckedTrackColor = Color(0xFF333333)
            )
        )
    }
    HorizontalDivider(thickness = 1.dp, color = Color(0xFF000000))
}