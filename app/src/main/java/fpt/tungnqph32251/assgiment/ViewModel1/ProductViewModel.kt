package fpt.tungnqph32251.assgiment.ViewModel1

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpt.tungnqph32251.assgiment.network.Product
import fpt.tungnqph32251.assgiment.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProductViewModel : ViewModel() {
    var productList by mutableStateOf<List<Product>>(emptyList()) // Danh sách sản phẩm

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getProducts()
                if (response.isSuccessful) {
                    val products = response.body()
                    if (products != null) {
                        productList = products
                        Log.d("ProductViewModel", "Products loaded: $products")
                    } else {
                        Log.e("ProductViewModel", "No products found")
                    }
                } else {
                    Log.e("ProductViewModel", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: HttpException) {
                Log.e("ProductViewModel", "HttpException: ${e.message}")
                e.printStackTrace()
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
