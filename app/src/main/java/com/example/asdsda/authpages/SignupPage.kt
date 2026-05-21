package com.example.asdsda.authpages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.asdsda.ExpandingLineLoader
import com.example.asdsda.models.AuthState
import com.example.asdsda.models.AuthViewModel
import com.example.asdsda.R

@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordsMatch by remember { mutableStateOf(true) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Authenticated -> navController.navigate("main")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF121212))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "ЗДЕСЬ РЕГАЕШЬСЯ",
            fontSize = 32.sp,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.srasb))
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Почта", color = Color.White) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (confirmPassword.isNotEmpty()) {
                    passwordsMatch = it == confirmPassword
                }
            },
            label = { Text("Пароль", color = Color.White) },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                passwordsMatch = password == it
            },
            label = { Text("Подтверждение пароля", color = Color.White) },
            isError = !passwordsMatch && confirmPassword.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Нажимая на кнопку вы подтверждаете что являетесь лохом",
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth())

        if (!passwordsMatch && confirmPassword.isNotEmpty()) {
            Text(
                text = "Пароли не совпадают",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                if (password != confirmPassword) {
                    passwordsMatch = false
                    Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                    return@OutlinedButton
                }
                authViewModel.signup(email, password)
            },
            enabled = authState.value != AuthState.Loading &&
                    email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    confirmPassword.isNotEmpty() &&
                    passwordsMatch,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RectangleShape,
        ) {
            if (authState.value == AuthState.Loading) {
                ExpandingLineLoader(
                    modifier = Modifier
                        .fillMaxWidth(1f),
                    isLoading = true,
                    lineColor = Color.White,
                    lineHeight = 2.dp
                )
            } else {
                Text("Зарегистрироваться", color = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ЧО АКК ЕСТЬ УЖЕ ДА?",
                color = Color.White
            )
        }
    }
}