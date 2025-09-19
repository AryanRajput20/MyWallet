package com.example.mywallet.navigation

import SplashScreen
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.mywallet.MainActivity
import com.example.mywallet.screens.*
import com.example.mywallet.viewmodel.CoinTransactionViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.example.mywallet.screens.LoginScreen


// ---------------------- SCREENS ----------------------
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object AuthChoice : Screen("auth_choice")
    object Register : Screen("register")
    object Login : Screen("login")
    object Home : Screen("home")
    object Transactions : Screen("transactions")
    object Profile : Screen("profile")

    object CoinDetail : Screen("coin_detail/{coinId}/{coinName}/{price}") {
        fun createRoute(coinId: String, coinName: String, price: Double) =
            "coin_detail/$coinId/$coinName/$price"
    }
}

// ---------------------- APP NAVIGATOR ----------------------
// ---------------------- APP NAVIGATOR ----------------------
@Composable
fun AppNavigator(
    auth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient,
    navController: NavHostController,
    coinTransactionViewModel: CoinTransactionViewModel
) {
    val startDestination =
        if (auth.currentUser != null) Screen.Home.route else Screen.Splash.route

    val context = LocalContext.current
    val activity = context as? MainActivity   // 👈 safe cast

    NavHost(navController = navController, startDestination = startDestination) {

        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }

        // Auth Choice
        composable(Screen.AuthChoice.route) {
            AuthChoiceScreen(
                onGoogleSignIn = { act ->
                    activity?.startGoogleSignIn(act)   // ✅ call MainActivity’s function
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }

        // Register
        composable(Screen.Register.route) {
            RegisterScreen(
                auth = auth,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.AuthChoice.route) { inclusive = true }
                    }
                }
            )
        }

        // Login
        composable(Screen.Login.route) {
            LoginScreen(
                auth = auth,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onGoogleSignIn = {
                    activity?.startGoogleSignIn(activity)  // ✅ clean Google Sign-In call
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Home
        composable(Screen.Home.route) {
            MainScreen(
                auth = auth,
                onLogout = {
                    auth.signOut()
                    navController.navigate(Screen.AuthChoice.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                appNavController = navController,
                viewModel = coinTransactionViewModel
            )
        }

        // Coin Detail
        composable(
            route = Screen.CoinDetail.route,
            arguments = listOf(
                navArgument("coinId") { type = NavType.StringType },
                navArgument("coinName") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val coinId = backStackEntry.arguments?.getString("coinId") ?: ""
            val coinName = backStackEntry.arguments?.getString("coinName") ?: ""
            val price = backStackEntry.arguments?.getString("price")?.toDoubleOrNull() ?: 0.0

            CoinDetailScreen(
                navController = navController,
                coinId = coinId,
                coinName = coinName,
                price = price,
                viewModel = coinTransactionViewModel
            )
        }

        // Transactions
        composable(Screen.Transactions.route) {
            TransactionsScreen(viewModel = coinTransactionViewModel)
        }
    }
}










