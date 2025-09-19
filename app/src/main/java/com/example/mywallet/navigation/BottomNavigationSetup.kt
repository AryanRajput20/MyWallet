package com.example.mywallet.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Default.Home, "Home"),
    BottomNavItem("transactions", Icons.Default.List, "Transactions"),
    BottomNavItem("profile", Icons.Default.Person, "Profile")
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}
