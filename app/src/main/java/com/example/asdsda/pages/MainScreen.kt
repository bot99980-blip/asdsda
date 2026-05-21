package com.example.asdsda.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.example.asdsda.R.drawable.rostic
import com.example.asdsda.dataclasses.CategoryRow
import com.example.asdsda.dataclasses.DelRow
import com.example.asdsda.dataclasses.MarketRow
import com.example.asdsda.dataclasses.SettingRow
import com.example.asdsda.models.AuthState
import com.example.asdsda.models.AuthViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    onMenuClick: () -> Unit,
    onNavigate: (String) -> Unit,
    noiseColor: Color = Color.White,
    noiseIntensity: Float = 1.0f
) {


    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            onNavigate("login")
        }
    }

    Column(
        modifier = Modifier
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    for (i in 0 until 1000) {
                        val x = Random.nextFloat() * size.width
                        val y = Random.nextFloat() * size.height
                        val alpha = Random.nextFloat() * noiseIntensity

                        drawCircle(
                            color = noiseColor.copy(alpha = alpha),
                            radius = 1f,
                            center = Offset(x, y)
                        )
                    }
                }
            }
            .fillMaxSize()
            .background(color = Color(0xFF121212))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = screenWidth * 0.05f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(
                            onClick = onMenuClick,
                        ),
                    contentDescription = "Меню",
                    painter = painterResource(R.drawable.menu)
                )
                Text(
                    text = "Новоселов 16, 18:35",
                    color = Color(0xFFFFFFFF),
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Image(
                    modifier = Modifier
                        .size(35.dp)
                        .clickable(
                            onClick = {
                                onNavigate("korzina")
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    contentDescription = "",
                    painter = painterResource(R.drawable.korzina)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            Search()
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp)
                .padding(top = 5.dp)
        ) {
            itemsIndexed(
                listOf(
                    CategoryRow(R.drawable.shawa, "Шаурма"),
                    CategoryRow(R.drawable.burg, "Бургеры"),
                    CategoryRow(R.drawable.pizza, "Пицца"),
                    CategoryRow(R.drawable.rolli, "Роллы"),
                    CategoryRow(R.drawable.sushi, "Суши"),
                    CategoryRow(R.drawable.fish, "Рыба"),
                    CategoryRow(R.drawable.free, "Снеки")
                )
            ) { _, item ->
                Category(item = item)
            }
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp)
                .padding(top = 5.dp)
        ) {
            itemsIndexed(
                listOf(
                    SettingRow(R.drawable.set1, ""),
                    SettingRow(R.drawable.set2, ""),
                    SettingRow(R.drawable.set3, "Доставка 0р"),
                    SettingRow(R.drawable.set4, "~30м"),
                    SettingRow(R.drawable.set5, "Акции"),
                    SettingRow(R.drawable.set6, "Рейтинг")
                )
            ) { _, item ->
                Settings(item = item)
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 15.dp, start = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                color = Color(0xFFFFFFFF),
                text = "Магазины",
                modifier = Modifier
                    .clickable(onClick = {
                    }),
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
            Image(
                modifier = Modifier.size(size = 35.dp),
                painter = painterResource(R.drawable.select),
                contentDescription = ""
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp)
        ) {
            itemsIndexed(
                listOf(
                    MarketRow(R.drawable.magnit, "~1,5ч", Color(0xFFFF0000)),
                    MarketRow(R.drawable.azbik, "~30м", Color(0xFF828F7A)),
                    MarketRow(R.drawable.vkus, "~1ч", Color(0xFF85C24F)),
                    MarketRow(R.drawable.azbik, "~1,5ч", Color(0xFF828F7A)),
                    MarketRow(R.drawable.xros, "~30м", Color(0xFF377019)),
                    MarketRow(R.drawable.lenta, "~1ч", Color(0xFF1100FF)),
                    MarketRow(R.drawable.ahan, "~2ч", Color(0xFFFF0000)),
                    MarketRow(R.drawable.dxsi, "~1,5ч", Color(0xFFF58220))
                )
            ) { _, item ->
                Market(item = item)
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 15.dp, start = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                color = Color(0xFFFFFFFF),
                text = "Популярное",
                modifier = Modifier
                    .clickable(onClick = {}),
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
            Image(
                modifier = Modifier
                    .size(size = 35.dp)
                    .rotate(degrees = 90.0F)
                    .clickable(onClick = {}),
                painter = painterResource(R.drawable.select),
                contentDescription = ""
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            RestaurantHeader(
                "ROSTIC'S",
                "Бесплатная доставка",
                "10-20 мин",
                4.8,
                525,
                rostic,
                "rostics",
                onNavigate = onNavigate
            )
            Spacer(modifier = Modifier.padding(bottom = 15.dp))
            RestaurantHeader(
                "Вкусно - и точка",
                "Бесплатная доставка",
                "30-40 мин",
                4.6,
                525,
                R.drawable.mac,
                "rostics",
                onNavigate = onNavigate
            )
            Spacer(modifier = Modifier.padding(bottom = 15.dp))
            RestaurantHeader(
                "Бургер Кинг",
                "Платная доставка",
                "5-6 ч",
                1.3,
                3,
                R.drawable.bking,
                "rostics",
                onNavigate = onNavigate
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 15.dp, start = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                color = Color(0xFFFFFFFF),
                text = "Бесплатная доставка",
                modifier = Modifier
                    .clickable(onClick = {
                    }),
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
            Image(
                modifier = Modifier.size(size = 35.dp),
                painter = painterResource(R.drawable.select),
                contentDescription = ""
            )
        }
        LazyRow(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            itemsIndexed(
                listOf(
                    DelRow(4.2, "Воккер", R.drawable.voker, "120-140 мин"),
                    DelRow(4.4, "Pizza Hut", R.drawable.pizhut, "70-100 мин"),
                    DelRow(4.3, "Техникум", R.drawable.tech, "90-120 мин"),
                    DelRow(4.7, "Kilim", R.drawable.kilim, "60-80 мин"),
                    DelRow(4.6, "Субабратик", R.drawable.tliso, "120-160 мин"),
                )
            ) { _, item ->
                Delifery(item = item)
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 5.dp, start = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                color = Color(0xFFFFFFFF),
                text = "Рестораны",
                modifier = Modifier
                    .clickable(onClick = {}),
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
            Image(
                modifier = Modifier
                    .size(size = 35.dp)
                    .rotate(degrees = 90.0F)
                    .clickable(onClick = {}),
                painter = painterResource(R.drawable.select),
                contentDescription = ""
            )
        }
        Column {
            Restaurants(
                "Пиццы, соусы, десерты",
                "40-50 мин",
                R.drawable.jons,
                "Папа Джонс",
                4.7
            )
            Restaurants(
                "Пиццы, роллы, суши, воки",
                "60-75 мин",
                R.drawable.pizashi,
                "PIZZASUSHIWOK",
                4.8
            )
            Restaurants(
                "Хинкали, cупы, десерты",
                "65-75 мин",
                R.drawable.hikali,
                "Хинкали Point",
                4.7
            )
            Restaurants(
                "Ягоды в шоколаде, десерты",
                "80-90 мин",
                R.drawable.godi,
                "Мир клубники в шоколаде",
                4.8
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(vertical = 40.dp),
                color = Color.White
            )
        }
    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Restaurants(
    categories: String,
    resttime: String,
    restImage: Int,
    name: String,
    rating: Double
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 20.dp, end = 20.dp),
    ) {
        Image(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(25.dp))
                .clickable(onClick = {})
                .fillMaxWidth(),
            painter = painterResource(restImage),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )
        Row(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                color = Color.White,
                text = name,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.srb)),
                modifier = Modifier
            )
            Text(
                text = "★$rating",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.srb)),
                color = Color.White
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = resttime,
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.srr)),
            )
            Text(
                text = categories,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.srr)),
                color = Color.White
            )
        }
    }
}


@SuppressLint("ConfigurationScreenWidthHeight", "SuspiciousIndentation")
@Composable
fun RestaurantHeader(
    name: String,
    deliveryInfo: String,
    deliveryTime: String,
    rating: Double,
    reviewsCount: Int,
    restImage: Int,
    route: String,
    onNavigate: (String) -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHight = configuration.screenHeightDp

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color(0xFF202020))
            .padding(vertical = screenWidth * 0.02f, horizontal = screenWidth * 0.02f)
            .clickable { onNavigate(route) },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .width(width = screenWidth * 0.4f)
                    .clip(RoundedCornerShape(30.dp)),
                painter = painterResource(restImage),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.padding(screenWidth * 0.01f))
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = name,
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    fontSize = (screenHight * 0.025).sp,
                    color = Color.White
                )

                Text(
                    text = deliveryInfo,
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    color = Color(0xFF000000),
                    fontSize = (screenHight * 0.015).sp,
                    modifier = Modifier
                        .padding(top = screenWidth * 0.014f)
                        .background(
                            color = Color(0xFFD5FFD6),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(end = screenWidth * 0.02f, start = screenWidth * 0.02f)

                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = screenWidth * 0.02f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = deliveryTime,
                        fontFamily = FontFamily(Font(R.font.srr)),
                        fontSize = (screenHight * 0.015).sp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "★",
                            color = Color(0xFFFFA000),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = (screenHight * 0.015).sp,
                        )
                        Text(
                            text = " ${rating} (${reviewsCount} отз.)",
                            fontFamily = FontFamily(Font(R.font.srasb)),
                            fontSize = (screenHight * 0.013).sp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Search() {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenHight = configuration.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(screenHeight * 0.07f)
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF1F1F1F),
                        Color(0xFF262626)
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            )
            .clickable(
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Поиск",
                tint = Color.White,
                modifier = Modifier.size(screenHeight * 0.04f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Поиск по еде",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.srr)),
                fontWeight = FontWeight.Normal,
                fontSize = (screenHight * 0.025).sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun Category(item: CategoryRow) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 11.dp, top = 11.dp, bottom = 11.dp)
    ) {
        Image(
            painter = painterResource(item.imageId),
            contentDescription = "",
            modifier = Modifier
                .size(90.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable(onClick = {})
        )
        Text(
            color = Color(0xFFFFFFFF),
            text = item.title,
            modifier = Modifier.padding(top = 3.dp),
            fontFamily = FontFamily(Font(R.font.srasb)),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}


@Composable
fun Settings(item: SettingRow) {

    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(top = 5.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    painter = painterResource(item.iconId),
                    contentDescription = "Быстрые настройки",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(size = 30.dp)
                        .clickable(onClick = { expanded = true })
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color(0xFF1E1E1E))
                ) {
                    listOf(
                        "🔥 Популярные" to "По рейтингу и количеству заказов",
                        "⚡ Быстрые" to "Минимальное время доставки",
                        "💰 Экономные" to "С бесплатной доставкой",
                        "⭐ Премиум" to "Рестораны премиум-класса",
                        "🍔 Ближайшие" to "По расстоянию до вас"
                    ).forEach { (title, description) ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = title,
                                        color = Color.White,
                                        fontFamily = FontFamily(Font(R.font.srasb)),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = description,
                                        color = Color(0xFFAAAAAA),
                                        fontFamily = FontFamily(Font(R.font.srr)),
                                        fontSize = 12.sp
                                    )
                                    Spacer(Modifier.padding(top = 5.dp))
                                    Divider(color = Color(0xFF333333))
                                    Spacer(Modifier.padding(top = 5.dp))
                                }
                            },
                            onClick = {
                                println("Применен пресет: $title")
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text(
                color = Color(0xFFFFFFFF),
                text = item.setting,
                modifier = Modifier.clickable(onClick = {}),
                fontFamily = FontFamily(Font(R.font.srb)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}


@Composable
fun Market(item: MarketRow) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 10.dp, start = 10.dp)
    )
    {
        Box(
            modifier = Modifier
                .width(width = 200.dp)
                .height(height = 80.dp)
                .clip(shape = RoundedCornerShape(25.dp))
                .background(item.backgroundColor)
                .padding(horizontal = 15.dp)
                .clickable(onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(item.marketId),
                contentDescription = "",
            )
        }
        Text(
            color = Color(0xFFFFFFFF),
            text = item.time,
            modifier = Modifier
                .padding(top = 3.dp)
                .align(Alignment.Start),
            fontFamily = FontFamily(Font(R.font.srasb)),
            fontSize = 16.sp
        )
    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Delifery(item: DelRow) {

    val configuration = LocalConfiguration.current
    val screenHight = configuration.screenHeightDp

    Column(
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp)
            .width(width = 215.dp),

        ) {
        Image(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(25.dp))
                .clickable(onClick = {}),
            painter = painterResource(item.delImage),
            contentDescription = "",
        )
        Row(
            Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column() {
                Text(
                    text = item.name,
                    fontFamily = FontFamily(Font(R.font.srm)),
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = item.deltime,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srr)),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .offset(y = (-6).dp)
                )
            }
            Text(
                text = "${item.rating} ★",
                fontFamily = FontFamily(Font(R.font.srasb)),
                color = Color.White,
            )
        }
    }
}
