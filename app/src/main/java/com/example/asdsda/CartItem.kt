package com.example.asdsda

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.asdsda.models.ProductModel
import com.example.asdsda.models.addItemToCart
import com.example.asdsda.models.removeFromCart
import com.example.asdsda.restaurants.ProductModal
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun CartItem(id : String, qty : Long) {

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHight = configuration.screenHeightDp
    val screenHeight = configuration.screenHeightDp.dp

    val isLoading = remember { mutableStateOf(true) }

    var product by remember {
        mutableStateOf(ProductModel())
    }

    LaunchedEffect(id) {
        Firebase.firestore.collection("data")
            .document("stock").collection("dish")
            .document(id)
            .addSnapshotListener { it, _ ->
                if(it!=null) {
                    isLoading.value = false
                    val result = it.toObject(ProductModel::class.java)
                    if (result!=null) {
                        product = result
                    }
                }
                else {
                    isLoading.value = false
                }
            }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidth * 0.025f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = product.image,
                    error = painterResource(R.drawable.icons8help),
                    placeholder = painterResource(R.drawable.icons8help)
                ),
                contentDescription = product.title,
                modifier = Modifier.size(size = 100.dp),
                contentScale = ContentScale.Fit
            )
            Column {
                Text(
                    text = product.title,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srm)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = product.price,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srexb)),
                    fontSize = 20.sp
                )
            }
            Row(
            ) {
                Text(
                    modifier = Modifier.clickable {
                        removeFromCart(id = product.id, context = context)
                    }
                    .padding(horizontal = 10.dp),
                    text = "-",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    text = qty.toString(),
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
                Text(
                    modifier = Modifier.clickable {
                        addItemToCart(id = product.id, context = context)
                    }
                        .padding(horizontal = 10.dp),
                    text = "+",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.srasb)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            }
        }
    }
}


