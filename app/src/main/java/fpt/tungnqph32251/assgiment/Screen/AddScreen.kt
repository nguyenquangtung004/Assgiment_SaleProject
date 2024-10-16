package fpt.tungnqph32251.assgiment.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import fpt.tungnqph32251.assgiment.ViewModel1.ProductViewModel
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun AddProductScreen(navController: NavHostController, productViewModel: ProductViewModel) {
    var name by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Thêm Sản Phẩm Mới",  // Tiêu đề mới rõ ràng hơn
            style = MaterialTheme.typography.h5,  // Điều chỉnh kiểu chữ
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)  // Tạo khoảng cách giữa tiêu đề và các thành phần khác
        )
        // Nhập tên sản phẩm
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Tên sản phẩm") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nhập URL hình ảnh
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL hình ảnh") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nhập giá sản phẩm
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Giá sản phẩm") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nhập mô tả sản phẩm
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Mô tả sản phẩm") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nhập danh mục sản phẩm
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Danh mục sản phẩm") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Nút gửi
        Button(
            onClick = {
                isSubmitting = true
                productViewModel.addProduct(
                    name = name,
                    imageUrl = imageUrl,
                    price = price.toDoubleOrNull() ?: 0.0,
                    description = description,
                    category = category,
                ) {
                    isSubmitting = false
                    navController.popBackStack() // Quay lại màn hình trước
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black, // Đặt màu nền của nút là màu đen
                contentColor = Color.White  // Màu chữ là màu trắng để tạo sự tương phản
            )
        )
        {
            Text("Thêm sản phẩm")
        }
    }
}
