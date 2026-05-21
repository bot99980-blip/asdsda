package com.example.asdsda.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.example.asdsda.CartItem
import com.example.asdsda.ExpandingLineLoader
import com.example.asdsda.R
import com.example.asdsda.models.UsersModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

@Composable
fun KorzinaScreen(
    onBackClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var clicked by remember { mutableStateOf(false) }
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
                text = "Корзина",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.srasb)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.Center)
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
        } else if (usersModel.value.cartItems.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Корзина пуста",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(usersModel.value.cartItems.toList(), key = { it.first}) { (id, qty) ->
                    CartItem(id = id, qty = qty)
                }
            }
        }
    }
}