package com.example.mywallet.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AuthChoiceScreen(
    onGoogleSignIn: (Activity) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit // Add this
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1E1E2C), Color(0xFF3A3A5C))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Google Sign-In
            Button(
                onClick = { /* Call main activity google sign in */ onGoogleSignIn(Activity()) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF4285F4)),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Sign in with Google", color = Color.White)
            }

            // Login with Email
            Button(
                onClick = onLoginClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50)),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Login with Email", color = Color.White)
            }

            // Register In-App
            Button(
                onClick = onRegisterClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6C63FF)),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Register In-App", color = Color.White)
            }
        }
    }
}




