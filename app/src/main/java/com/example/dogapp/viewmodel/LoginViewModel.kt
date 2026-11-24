package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dogapp.repository.LoginRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val loginRepository = LoginRepository(context)

    private val _authSuccess = MutableLiveData(false)
    val authSuccess: LiveData<Boolean> get() = _authSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun isBiometricAvailable(): Boolean {
        return loginRepository.isBiometricAvailable()
    }

    fun onAuthenticationSucceeded() {
        _authSuccess.value = true
    }

    fun onAuthenticationError(message: String) {
        _errorMessage.value = message
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
