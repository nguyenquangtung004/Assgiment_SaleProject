package fpt.tungnqph32251.assgiment.Screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.* // Import all layout functions at once
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.* // Import mutableStateOf and related functions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import fpt.tungnqph32251.assgiment.Model.Product
import fpt.tungnqph32251.assgiment.R
import fpt.tungnqph32251.assgiment.Screen.ui.theme.AssgimentTheme

class CartScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssgimentTheme {
                val navController = rememberNavController() // Khởi tạo NavHostController
                Cart(navController = navController)
            }
        }
    }
}

@Composable
fun Cart(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)

    // Lấy danh sách sản phẩm từ SharedPreferences
    var productList by remember {
        mutableStateOf(
            Gson().fromJson(sharedPreferences.getString("cartItems", "[]"), Array<Product>::class.java).toList()
        )
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPaymentSuccess by remember { mutableStateOf(false) } // Trạng thái hiển thị thông báo thành công
    var productToDelete by remember { mutableStateOf<Product?>(null) }
    var showPaymentDialog by remember { mutableStateOf(false) } // Trạng thái mở hộp thoại thanh toán

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Giỏ Hàng",
            style = MaterialTheme.typography.headlineLarge,  // Thay đổi kích thước chữ
            color = Color.Black,  // Màu sắc của tiêu đề
            modifier = Modifier
                .fillMaxWidth()  // Tiêu đề chiếm hết chiều rộng của màn hình
                .padding(vertical = 16.dp),  // Thêm khoảng cách phía trên và dưới
            textAlign = TextAlign.Center  // Căn giữa tiêu đề
        )

        // Nếu giỏ hàng trống, hiển thị thông báo và nút về trang chủ
        if (productList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),  // Cột chiếm toàn bộ màn hình
                verticalArrangement = Arrangement.Center,  // Căn giữa theo chiều dọc
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Giỏ hàng rỗng",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center

                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("home") }, // Điều hướng về màn hình Home
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(text = "Về trang chủ", color = Color.White)
                }
            }

        } else {
            // Thay thế LazyColumn bằng Column kết hợp với verticalScroll
            val scrollState = rememberScrollState() // Tạo ScrollState để cuộn nội dung

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState), // Thêm khả năng cuộn
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                productList.forEach { product ->
                    CartItem(
                        name = product.name,
                        price = product.price,
                        imageUrl = null,
                        onDeleteClick = {
                            // Hiển thị hộp thoại xóa
                            productToDelete = product
                            showDeleteDialog = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tổng cộng
            CartSummary(subtotal = productList.sumByDouble { it.price }, delivery = 10.0, tax = 2.2)

            Spacer(modifier = Modifier.height(16.dp))

            // Nút "Check Out"
            Button(
                onClick = { showPaymentDialog = true }, // Mở hộp thoại thanh toán khi nhấn nút "Thanh Toán"
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Thanh Toán", color = Color.White)
            }

            // Hiển thị hộp thoại thanh toán
            if (showPaymentDialog) {
                PaymentDialog(
                    onDismiss = { showPaymentDialog = false },  // Đóng hộp thoại thanh toán
                    onPaymentOptionSelected = { option ->
                        if (option == "Thanh toán tại nhà") {
                            // Xóa toàn bộ sản phẩm trong giỏ hàng
                            productList = emptyList()

                            // Cập nhật SharedPreferences sau khi thanh toán
                            val editor = sharedPreferences.edit()
                            editor.putString("cartItems", Gson().toJson(productList))
                            editor.apply()

                            // Hiển thị thông báo thanh toán thành công
                            showPaymentSuccess = true
                        }
                        showPaymentDialog = false
                    }
                )
            }

            // Hiển thị thông báo thanh toán thành công
            if (showPaymentSuccess) {
                AlertDialog(
                    onDismissRequest = { showPaymentSuccess = false },
                    title = { Text("Thông báo") },
                    text = { Text("Thanh toán thành công!") },
                    confirmButton = {
                        Button(onClick = { showPaymentSuccess = false }) {
                            Text("OK")
                        }
                    }
                )
            }

            // Hiển thị hộp thoại xác nhận xóa nếu có sản phẩm cần xóa
            if (showDeleteDialog && productToDelete != null) {
                ConfirmDeleteDialog(
                    onConfirm = {
                        // Xóa sản phẩm khỏi danh sách và SharedPreferences
                        productList = productList.filter { it != productToDelete }

                        // Cập nhật SharedPreferences sau khi xóa
                        val editor = sharedPreferences.edit()
                        editor.putString("cartItems", Gson().toJson(productList))
                        editor.apply()

                        showDeleteDialog = false
                        productToDelete = null
                    },
                    onDismiss = {
                        showDeleteDialog = false
                        productToDelete = null
                    }
                )
            }
        }
    }
}



@Composable
fun CartItem(name: String, price: Double, imageUrl: String?, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp), // Thiết lập độ đổ bóng cho Card
        shape = MaterialTheme.shapes.medium // Bo góc cho Card
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Khoảng cách bên trong Card
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin sản phẩm
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$$price",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF6200EE)
                )
            }

            // Nút Xóa
            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Xác nhận xóa") },
        text = { Text(text = "Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?") },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(text = "Xóa")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Hủy")
            }
        }
    )
}

@Composable
fun CartSummary(subtotal: Double, delivery: Double, tax: Double) {
    val calculatedDelivery = if (subtotal > 0) delivery else 0.0
    val calculatedTax = if (subtotal > 0) tax else 0.0
    val totalAmount = subtotal + calculatedDelivery + calculatedTax

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Tổng cộng
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tổng cộng")
            Text(text = "$$subtotal")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Phí vận chuyển
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Phí vận chuyển")
            Text(text = "$$calculatedDelivery")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Thuế
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Thuế")
            Text(text = "$$calculatedTax")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tổng tiền
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tổng tiền", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "$$totalAmount",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun PaymentDialog(onDismiss: () -> Unit, onPaymentOptionSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Chọn phương thức thanh toán") },
        text = {
            Column {
                Button(
                    onClick = { onPaymentOptionSelected("Thanh toán tại nhà") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Thanh toán tại nhà")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onPaymentOptionSelected("Thanh toán bằng thẻ Visa") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Thanh toán bằng thẻ Visa")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Hủy")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    val navController = rememberNavController() // Khởi tạo NavHostController
    Cart(navController = navController)
}
