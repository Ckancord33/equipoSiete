package com.example.dogapp.repository

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK

class LoginRepository(val context: Context) {
    private val biometricManager: BiometricManager = BiometricManager.from(context)

    fun isBiometricAvailable(): Boolean {
        return biometricManager.canAuthenticate(BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }
}
