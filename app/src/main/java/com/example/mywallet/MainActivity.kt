package com.example.mywallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mywallet.navigation.AppNavigator
import com.example.mywallet.ui.theme.MyWalletTheme
import com.example.mywallet.viewmodel.CoinTransactionViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    var navControllerRef: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            MyWalletTheme {
                val navController = rememberNavController()
                val viewModel: CoinTransactionViewModel = viewModel() // ✅ create ViewModel

                SideEffect { navControllerRef = navController }

                AppNavigator(
                    auth = auth,
                    googleSignInClient = googleSignInClient,
                    navController = navController,
                    coinTransactionViewModel = viewModel // ✅ pass ViewModel here
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            if (task.isSuccessful) {
                val account = task.result
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful) {
                            Toast.makeText(this, "Google Sign-In Success", Toast.LENGTH_SHORT).show()
                            navControllerRef?.navigate("home") {
                                popUpTo("auth_choice") { inclusive = true }
                            } ?: Toast.makeText(this, "NavController not ready", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Google Account Null", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Google Sign-In Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun startGoogleSignIn(activity: Activity) {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1001
    }
}
















