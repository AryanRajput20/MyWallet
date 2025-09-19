package com.example.mywallet.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mywallet.navigation.BottomNavigationBar
import com.example.mywallet.navigation.Screen
import com.example.mywallet.viewmodel.CoinTransactionViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen(
    auth: FirebaseAuth,
    onLogout: () -> Unit,
    appNavController: NavHostController,
    viewModel: CoinTransactionViewModel
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(bottomNavController) }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    bottomNavController = bottomNavController,
                    appNavController = appNavController,
                    viewModel = viewModel
                )
            }

            composable(Screen.Transactions.route) {
                TransactionsScreen(viewModel = viewModel)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    auth = auth,
                    viewModel = viewModel, // ✅ Pass viewModel here
                    onLogout = onLogout
                )
            }
        }
    }
}








