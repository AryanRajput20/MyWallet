package com.example.mywallet.viewmodel

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: () -> Unit,
        onVerificationFailed: (String) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential, activity)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("Auth", "Verification Failed: ${e.message}")
                    onVerificationFailed(e.message ?: "Unknown error")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    resendToken = token
                    onCodeSent()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(code: String, activity: Activity, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val credential = storedVerificationId?.let { PhoneAuthProvider.getCredential(it, code) }
        if (credential != null) {
            signInWithCredential(credential, activity, onSuccess, onFailure)
        } else {
            onFailure("Verification ID is null")
        }
    }

    private fun signInWithCredential(
        credential: PhoneAuthCredential,
        activity: Activity,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Login failed")
                }
            }
    }
}
