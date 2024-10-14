package fpt.tungnqph32251.assgiment.Screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fpt.tungnqph32251.assgiment.R
import fpt.tungnqph32251.assgiment.Screen.ui.theme.AssgimentTheme

class Favourite : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssgimentTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar(title = "Yêu Thích") } // Gọi TopBar ở đây
                ) { innerPadding -> // Nhận giá trị padding từ Scaffold
                    FavouriteScreen(modifier = Modifier.padding(innerPadding)) // Áp dụng innerPadding
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun FavouriteScreen(modifier: Modifier = Modifier) {
    // Sử dụng MutableStateList để quản lý danh sách sản phẩm
    var favouriteProducts by remember { mutableStateOf(listOf(
        Product("Stylish Plaid Shirt", 35.0, R.drawable.prd1),
        Product("Cozy Knit Sweater", 75.0, R.drawable.prd2)
    )) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(favouriteProducts) { product ->
            FavouriteItem(product = product, onDelete = {
                favouriteProducts = favouriteProducts.filter { it != product }
            })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FavouriteItem(product: Product, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cột bên trái - Hình ảnh sản phẩm
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 16.dp)
        )

        // Cột bên phải - Tên và giá sản phẩm
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6200EE)
            )
        }

        // Nút xóa
        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Xóa sản phẩm",
                tint = Color.Red
            )
        }
    }
}

data class Product(
    val name: String,
    val price: Double,
    val imageRes: Int
)

@Preview(showBackground = true)
@Composable
fun PreviewFavouriteScreen() {
    AssgimentTheme {
        Scaffold(
            topBar = { TopBar(title = "Yêu Thích") }
        ) { innerPadding -> // Nhận innerPadding từ Scaffold
            FavouriteScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
