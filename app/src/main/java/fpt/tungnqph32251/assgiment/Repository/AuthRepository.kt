package fpt.tungnqph32251.assgiment.repository

import fpt.tungnqph32251.assgiment.network.ApiService
import fpt.tungnqph32251.assgiment.network.AuthResponse
import fpt.tungnqph32251.assgiment.network.LoginRequest
import fpt.tungnqph32251.assgiment.network.RegisterRequest
import fpt.tungnqph32251.assgiment.network.RetrofitInstance
import retrofit2.Response

class AuthRepository {
    private val api = RetrofitInstance.api

    // Đăng ký người dùng
    suspend fun registerUser(name: String, username: String, email: String, password: String): Response<AuthResponse> {
        val request = RegisterRequest(name, username, email, password)
        return api.registerUser(request)
    }

    // Đăng nhập người dùng
    suspend fun loginUser(email: String, password: String): Response<AuthResponse> {
        val request = LoginRequest(email, password)
        return api.loginUser(request)
    }
}
