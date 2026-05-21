package com.example.asdsda

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.asdsda.authpages.LoginPage
import com.example.asdsda.authpages.SignupPage
import com.example.asdsda.models.AuthState
import com.example.asdsda.models.AuthViewModel
import com.example.asdsda.pages.KorzinaScreen
import com.example.asdsda.pages.MainScreen
import com.example.asdsda.pages.NoticScreen
import com.example.asdsda.pages.OrdersScreen
import com.example.asdsda.pages.ProfileScreen
import com.example.asdsda.pages.SettingsScreen
import com.example.asdsda.restaurants.Rostics
import com.example.asdsda.ui.theme.AsdsdaTheme
import kotlinx.coroutines.launch
import kotlin.String


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        val authViewModel: AuthViewModel by viewModels()

        setTheme(R.style.Theme_Asdsda)

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {


            AsdsdaTheme(darkTheme = isSystemInDarkTheme()) {

                setTheme(R.style.Theme_Asdsda)

                val context = LocalContext.current
                val authState = authViewModel.authState.observeAsState()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val sheetState = rememberModalBottomSheetState()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination
                val isLoginScreen = currentDestination?.route == "login"
                var isSheetOpen by rememberSaveable {
                    mutableStateOf(false)
                }


                val startDestination = when (authState.value) {
                    is AuthState.Authenticated -> "main"
                    is AuthState.Unauthenticated -> "login"
                    else -> "login"
                }

                val navigationHandler = { route: String ->
                    scope.launch {
                        drawerState.close()
                    }


                    if (currentDestination?.route != route) {
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                }



                LaunchedEffect(authState.value) {

                    val currentAuthState = authState.value
                    val currentRoute = navController.currentDestination?.route

                    when (currentAuthState) {
                        is AuthState.Authenticated -> {
                            if (currentRoute != "main") {
                                navController.navigate("main") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }

                        is AuthState.Unauthenticated -> {
                            if (currentRoute != "login") {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }

                        is AuthState.Error -> {
                            if (currentRoute == "login" || currentRoute == "signup") {
                                Toast.makeText(
                                    context,
                                    currentAuthState.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        else -> {}
                    }
                }



                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = authState.value is AuthState.Authenticated,
                    drawerContent = {
                        DrawerContent(
                            authViewModel = authViewModel,
                            onItemClick = navigationHandler,
                            onCloseClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                    }
                ) {

                    NavHost(
                        navController, startDestination = startDestination
                    )
                    {

                        composable(
                            "login"
                        ) {
                            LoginPage(
                                modifier = Modifier,
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }


                        composable(
                            "signup"
                        ) {
                            SignupPage(
                                modifier = Modifier,
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }


                        composable(route = "main") {
                            MainScreen(
                                authViewModel = authViewModel,
                                onMenuClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo("main") { inclusive = false }
                                    }
                                }
                            )
                        }


                        composable(
                            route = "profile",
                        ) {
                            ProfileScreen(
                                authViewModel = authViewModel,
                                onBackClick = { navController.popBackStack() },
                                onItemClick = { drawerItems ->
                                    when (drawerItems) {
                                        "settings" -> {
                                            navController.navigate("settings")
                                        }

                                        "login" -> {
                                            navController.navigate("login")
                                        }
                                    }
                                }
                            )
                        }


                        composable(
                            route = "orders",
                        ) {
                            OrdersScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }


                        composable(
                            route = "settings",
                        ) {
                            SettingsScreen(
                                onBackClick = { navController.popBackStack() },
                                authViewModel = authViewModel
                            )
                        }


                        composable(
                            route = "korzina",
                        ) {
                            KorzinaScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }


                        composable(
                            route = "rings",
                        ) {
                            NoticScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }


                        composable(route = "rostics") {
                            Rostics(
                                navController = navController,
                                onBackClick = { navController.popBackStack() },
                            )
                        }


                        composable(route = "splash") {
                            SplashScreenContent()
                        }
                    }
                }
            }
        }
    }
}