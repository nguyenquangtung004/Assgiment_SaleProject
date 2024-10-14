package fpt.tungnqph32251.assgiment.Screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // Import all layout functions at once
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import fpt.tungnqph32251.assgiment.Screen.ui.theme.AssgimentTheme

class ProductDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssgimentTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        ProductDetailScreen(
                            navController = navController,
                            backStackEntry = navController.previousBackStackEntry!!,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                )
            }
        }
    }
}

// Đổi tên class thành ProductDetail2 hoặc ProductItem (hoặc một tên khác nếu muốn)
data class ProductItem(
    val name: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val category: String
)

@Composable
fun ProductDetailScreen(navController: NavHostController, backStackEntry: NavBackStackEntry, modifier: Modifier = Modifier) {
    val productName = backStackEntry.arguments?.getString("name") ?: ""
    val productImageUrl = backStackEntry.arguments?.getString("image_url") ?: ""
    val productPrice = backStackEntry.arguments?.getString("price")?.toDouble() ?: 0.0
    val productDescription = backStackEntry.arguments?.getString("description") ?: ""
    val productCategory = backStackEntry.arguments?.getString("category") ?: ""
    val productRatings = backStackEntry.arguments?.getString("ratings")?.toFloat() ?: 0f

    val context = LocalContext.current

    // Hiển thị chi tiết sản phẩm với các dữ liệu nhận được
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF8F9FA)) // Màu nền sáng nhẹ cho toàn màn hình
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberImagePainter(productImageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(12.dp)) // Bo góc hình ảnh
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = productName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            fontSize = 32.sp, // Điều chỉnh kích thước chữ
            color = Color(0xFF333333) // Màu chữ tối
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Đánh giá: ${productRatings.toString()}",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF666666) // Màu chữ trung tính cho thông tin phụ
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Giá: $productPrice",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Red),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium // Chữ in đậm cho giá sản phẩm
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mô tả:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF333333)
        )
        Text(
            text = productDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666)
        )

        Spacer(modifier = Modifier.height(16.dp))

//        Text(
//            text = "Danh mục: $productCategory",
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color(0xFF666666)
//        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nút "Mua hàng"
        Button(
            onClick = {
                // Lấy danh sách sản phẩm hiện có từ SharedPreferences
                val sharedPreferences: SharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                // Tạo đối tượng sản phẩm mới
                val newProduct =  ProductItem(
                    name = productName,
                    price = productPrice,
                    imageUrl = productImageUrl,
                    description = productDescription,
                    category = productCategory
                )

                // Lấy danh sách sản phẩm hiện tại từ SharedPreferences
                val existingProducts = sharedPreferences.getString("cartItems", "[]")
                val productList = Gson().fromJson(existingProducts, Array<ProductItem>::class.java).toMutableList()

                // Thêm sản phẩm mới vào danh sách
                productList.add(newProduct)

                // Lưu lại danh sách sản phẩm đã cập nhật
                editor.putString("cartItems", Gson().toJson(productList))
                editor.apply()

                // Hiển thị thông báo mua hàng thành công
                Toast.makeText(context, "Mua hàng thành công", Toast.LENGTH_SHORT).show()

                // Điều hướng tới màn hình giỏ hàng
                navController.navigate("cart")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Chiều cao nút
                .padding(horizontal = 16.dp) // Khoảng cách từ cạnh màn hình
                .clip(RoundedCornerShape(25.dp)), // Bo góc nút
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black) // Màu tím nổi bật cho nút
        ) {
            Text(text = "Mua hàng", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    AssgimentTheme {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                ProductDetailScreen(
                    navController = navController,
                    backStackEntry = navController.previousBackStackEntry!!,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        )
    }
}