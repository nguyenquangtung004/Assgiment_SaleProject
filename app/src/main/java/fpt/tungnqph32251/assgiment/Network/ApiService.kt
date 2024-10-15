package fpt.tungnqph32251.assgiment.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Các model dữ liệu
data class RegisterRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String
)

// Model Product để ánh xạ dữ liệu từ API
data class Product(
    val name: String,         // Tên sản phẩm
    val image_url: String,    // URL hình ảnh của sản phẩm
    val price: Double,        // Giá của sản phẩm
    val description: String,  // Mô tả của sản phẩm
    val category: String,     // Danh mục sản phẩm
    val ratings: Float        // Số lượng đánh giá
)

// Interface chứa các API service
interface ApiService {

    // API đăng ký
    @POST("/users/reg")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>

    // API đăng nhập
    @POST("/users/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    // API lấy danh sách sản phẩm
    @GET("/products")
    suspend fun getProducts(): Response<List<Product>> // Trả về danh sách sản phẩm

    // Thêm hàm tìm kiếm sản phẩm vào interface ApiService
    @GET("/products/search")
    suspend fun searchProduct(@Query("q") query: String): Response<List<Product>>


}

// Đối tượng Retrofit để tạo các API service
object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.41:3000/") // Địa chỉ API của bạn
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
