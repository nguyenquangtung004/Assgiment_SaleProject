package fpt.tungnqph32251.assgiment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpt.tungnqph32251.assgiment.network.AuthResponse
import fpt.tungnqph32251.assgiment.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    var authToken: String? = null
    var errorMessage: String? = null

    fun registerUser(name: String, username: String, email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val response = repository.registerUser(name, username, email, password)
            handleResponse(response, onSuccess, onError)
        }
    }

    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val response = repository.loginUser(email, password)
            handleResponse(response, onSuccess, onError)
        }
    }

    private fun handleResponse(response: Response<AuthResponse>, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (response.isSuccessful) {
            authToken = response.body()?.token
            onSuccess()
        } else {
            errorMessage = "Lỗi: ${response.message()}"
            onError(errorMessage ?: "Unknown Error")
        }
    }
}
