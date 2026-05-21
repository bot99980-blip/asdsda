package com.example.asdsda.restaurants

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.asdsda.ExpandingLineLoader
import com.example.asdsda.R
import com.example.asdsda.models.ProductModel
import com.example.asdsda.models.UsersModel
import com.example.asdsda.models.addItemToCart
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlin.let

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Rostics(
    onBackClick: () -> Unit,
    navController: NavController
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHight = configuration.screenHeightDp
    val screenHeight = configuration.screenHeightDp.dp

    var clicked by remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }
    val usersModel = remember { mutableStateOf(UsersModel()) }
    var selectedProduct by remember { mutableStateOf<ProductModel?>(null) }
    var showModal by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
            .background(color = Color(0xFF121212))
            .fillMaxSize()
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.bkrost),
                contentDescription = "Фон",
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                contentScale = ContentScale.Fit
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 25.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = screenWidth * 0.1f),
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row {
                                Icon(
                                    painter = painterResource(R.drawable.back),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(size = screenWidth * 0.08f)
                                        .clickable {
                                            if (!clicked) {
                                                clicked = true
                                                onBackClick()
                                            }
                                        },
                                    tint = Color.White
                                )
                            }
                            Row() {
                                Icon(
                                    painter = painterResource(R.drawable.heart),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(end = 20.dp)
                                        .size(size = screenWidth * 0.08f),
                                    tint = Color.White
                                )
                                if (usersModel.value.cartItems.isEmpty()) {
                                    Box() {
                                        Image(
                                            modifier = Modifier
                                                .padding(end = 20.dp)
                                                .size(size = screenWidth * 0.08f)
                                                .clickable(
                                                    onClick = {
                                                        navController.navigate("korzina")
                                                    },
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ),
                                            contentDescription = "",
                                            painter = painterResource(R.drawable.korzina)
                                        )

                                    }
                                } else {
                                    Box(
                                        contentAlignment = Alignment.TopEnd
                                    ) {
                                        Image(
                                            modifier = Modifier
                                                .padding(end = 20.dp)
                                                .size(size = screenWidth * 0.08f)
                                                .clickable(
                                                    onClick = {
                                                        navController.navigate("korzina")
                                                    },
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ),
                                            contentDescription = "",
                                            painter = painterResource(R.drawable.korzina)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .padding(end = 20.dp)
                                                .size(size = 20.dp)
                                                .clip(shape = RoundedCornerShape(15.dp))
                                                .background(color = Color.White),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = usersModel.value.cartItems.values.sum().toString(),
                                                color = Color.Black,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
//                                Icon(
//                                    painter = painterResource(R.drawable.korzina),
//                                    contentDescription = "",
//                                    modifier = Modifier
//                                        .clickable {
//                                            navController.navigate("korzina")
//                                        }
//                                        .padding(end = 20.dp)
//                                        .size(size = screenWidth * 0.08f),
//                                    tint = Color.White
//                                )
                                Icon(
                                    painter = painterResource(R.drawable.search),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(size = screenWidth * 0.08f),
                                    tint = Color.White
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .padding(
                                    top = screenHeight * 0.015f,
                                    bottom = screenHeight * 0.01f
                                )
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "ROSTIC'S",
                                fontFamily = FontFamily(Font(R.font.srexb)),
                                fontSize = (screenHight * 0.05f).sp,
                                color = Color.White
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "4.7★",
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.srr)),
                                    fontSize = (screenHight * 0.025).sp,
                                )
                                Text(
                                    text = "500+ оценок",
                                    color = Color.Gray,
                                    fontFamily = FontFamily(Font(R.font.srr)),
                                    fontSize = (screenHight * 0.013).sp,
                                    lineHeight = 1.sp
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "8:00-23:00",
                                    color = Color.White,
                                    textAlign = TextAlign.Right,
                                    fontFamily = FontFamily(Font(R.font.srr)),
                                    fontSize = (screenHight * 0.025).sp,
                                )
                                Text(
                                    text = "время работы доставки",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Right,
                                    fontFamily = FontFamily(Font(R.font.srr)),
                                    fontSize = (screenHight * 0.013).sp,
                                    lineHeight = 1.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        val productList = remember { mutableStateOf<List<ProductModel>>(emptyList()) }

        LaunchedEffect(key1 = Unit) {
            Firebase.firestore.collection("data").document("stock")
                .collection("dish")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isLoading.value = false
                        val resultList = it.result.documents.mapNotNull { doc ->
                            doc.toObject(ProductModel::class.java)
                        }
                        productList.value = resultList
                    }
                    else {
                        isLoading.value = false
                    }
                }
        }

        if (showModal && selectedProduct != null) {
            ProductModal(
                product = selectedProduct!!,
                onDismiss = {
                    showModal = false
                    selectedProduct = null
                }
            )
        }

        if (isLoading.value) {
            ExpandingLineLoader(
                modifier = Modifier
                    .fillMaxWidth(1f),
                isLoading = true,
                lineColor = Color.White,
                lineHeight = 2.dp
            )
        }
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = screenWidth * 0.025f)
                    .clip(shape = RoundedCornerShape(25.dp))
            ) {
                items(productList.value) { product : ProductModel ->
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .clickable {
                                selectedProduct = product
                                showModal = true
                            }
                    ) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = product.image,
                                    error = painterResource(R.drawable.logo),
                                    placeholder = painterResource(R.drawable.logo)
                                ), "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(225.dp)
                                    .clip(shape = RoundedCornerShape(25.dp))
                                    .background(color = Color(0xFF202020))
                                    .padding(10.dp)
                            )
                            IconButton(
                                onClick = {
                                    addItemToCart(context = context,id = product.id)
                                },
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(15.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.choose),
                                    "",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Column(
                            Modifier.padding(start = screenWidth * 0.025f, top = 6.dp, bottom = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    fontFamily = FontFamily(Font(R.font.srasb)),
                                    fontSize = (screenHight * 0.025f).sp,
                                    text = product.price,
                                    color = Color.White
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(start = 5.dp),
                                    text = product.weight,
                                    fontFamily = FontFamily(Font(R.font.srr)),
                                    fontSize = (screenHight * 0.015f).sp,
                                    color = Color.LightGray
                                )
                            }
                            Text(
                                fontFamily = FontFamily(Font(R.font.srr)),
                                fontSize = (screenHight * 0.018f).sp,
                                text = product.title,
                                color = Color.LightGray,
                                lineHeight = 25.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductModal(
    product: ProductModel,
    onDismiss: () -> Unit
) {

    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color(0xFF121212)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = product.image,
                        error = painterResource(R.drawable.logo),
                        placeholder = painterResource(R.drawable.logo)
                    ),
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product.price,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.srb))
                        )
                        Text(
                            text = product.weight,
                            color = Color.Gray,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.srr))
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.title,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontFamily = FontFamily(Font(R.font.srb)),
                    )
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            if (product.disc.isNotEmpty()) {
                Text(
                    text = product.disc,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.srm)),
                    lineHeight = 20.sp
                )
            } else {
                Text(
                    text = "Описание отсутствует",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.srr))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    addItemToCart(id = product.id, context)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Добавить в корзину",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.srasb))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}