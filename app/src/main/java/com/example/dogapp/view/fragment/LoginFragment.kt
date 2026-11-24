package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentLoginBinding
import com.example.dogapp.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarBiometria()
        controladores()
        observerViewModel()
    }

    private fun configurarBiometria() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    loginViewModel.onAuthenticationSucceeded()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    val cancelado = errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                            errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                            errorCode == BiometricPrompt.ERROR_CANCELED
                    if (!cancelado) {
                        loginViewModel.onAuthenticationError(errString.toString())
                    }
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.login_biometric_title))
            .setSubtitle(getString(R.string.login_biometric_subtitle))
            .setNegativeButtonText(getString(R.string.login_biometric_cancel))
            .build()
    }

    private fun controladores() {
        binding.lottieFingerprint.setOnClickListener {
            autenticar()
        }
    }

    private fun autenticar() {
        if (loginViewModel.isBiometricAvailable()) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            showDialogBiometriaNoDisponible()
        }
    }

    private fun showDialogBiometriaNoDisponible() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setTitle(R.string.login_biometric_unavailable_title)
        builder.setMessage(R.string.login_biometric_unavailable_message)
        builder.setNegativeButton(R.string.login_biometric_cancel) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun observerViewModel() {
        observerAuthSuccess()
        observerErrorMessage()
    }

    private fun observerAuthSuccess() {
        loginViewModel.authSuccess.observe(viewLifecycleOwner) { success ->
            val navController = findNavController()
            if (success && navController.currentDestination?.id == R.id.loginFragment) {
                navController.navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    private fun observerErrorMessage() {
        loginViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                loginViewModel.clearErrorMessage()
            }
        }
    }
}
