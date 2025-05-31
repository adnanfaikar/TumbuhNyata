package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tumbuhnyata.data.model.RegisterRequest
import com.example.tumbuhnyata.data.model.RegisterResponse
import com.example.tumbuhnyata.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    
    // State for the registration form
    private val _companyName = MutableStateFlow("")
    val companyName: StateFlow<String> = _companyName
    
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber
    
    private val _nib = MutableStateFlow("")
    val nib: StateFlow<String> = _nib
    
    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    
    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep
    
    // UI states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    
    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess
    
    // Update functions
    fun updateCompanyName(value: String) {
        _companyName.value = value
    }
    
    fun updateEmail(value: String) {
        _email.value = value
    }
    
    fun updatePhoneNumber(value: String) {
        _phoneNumber.value = value
    }
    
    fun updateNIB(value: String) {
        _nib.value = value
    }
    
    fun updateAddress(value: String) {
        _address.value = value
    }
    
    fun updatePassword(value: String) {
        _password.value = value
    }
    
    fun nextStep() {
        if (_currentStep.value < 3) {
            _currentStep.value += 1
        }
    }
    
    fun previousStep() {
        if (_currentStep.value > 1) {
            _currentStep.value -= 1
        }
    }
    
    fun register() {
        // Validation
        if (password.value.length < 8) {
            _errorMessage.value = "Kata sandi harus minimal 8 karakter"
            return
        }
        
        if (nib.value.isEmpty()) {
            _errorMessage.value = "NIB tidak boleh kosong"
            return
        }
        
        if (nib.value.length > 13) {
            _errorMessage.value = "NIB tidak boleh lebih dari 13 karakter"
            return
        }
        
        val registerRequest = RegisterRequest(
            companyName = companyName.value,
            email = email.value,
            password = password.value,
            phoneNumber = phoneNumber.value,
            NIB = nib.value,
            address = address.value
        )
        
        _isLoading.value = true
        _errorMessage.value = null
        
        RetrofitInstance.api.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                
                if (response.isSuccessful) {
                    _registerSuccess.value = true
                } else {
                    try {
                        _errorMessage.value = response.errorBody()?.string() ?: "Pendaftaran gagal"
                    } catch (e: Exception) {
                        _errorMessage.value = "Pendaftaran gagal: ${response.code()}"
                    }
                }
            }
            
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Tidak dapat terhubung ke server: ${t.localizedMessage}"
            }
        })
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
} 