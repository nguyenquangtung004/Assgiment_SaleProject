package fpt.tungnqph32251.assgiment.Screen

import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import fpt.tungnqph32251.assgiment.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.launch
import fpt.tungnqph32251.assgiment.Screen.ui.theme.AssgimentTheme
import fpt.tungnqph32251.assgiment.ViewModel1.ProductViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssgimentTheme {
                // Khởi tạo NavController để điều hướng giữa các màn hình
                val navController = rememberNavController()

                // Scaffold chứa bố cục chính của ứng dụng, bao gồm bottom bar và phần nội dung chính
                Scaffold(
                    bottomBar = { BottomAppBarContent(navController, "Home") } // Thiết lập thanh điều hướng dưới cùng
                ) {
                    NavHostScreen(navController = navController, productViewModel = ProductViewModel()) // Đặt NavHost để điều hướng màn hình
                }

            }
        }
    }
}

@Composable
fun NavHostScreen(navController: NavHostController, productViewModel: ProductViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                productViewModel = productViewModel
            )
        }
        composable(
            "productDetail/{name}/{image_url}/{price}/{description}/{category}/{ratings}"
        ) { backStackEntry ->
            ProductDetailScreen(navController = navController, backStackEntry = backStackEntry)
        }
        composable("cart") {
            Cart() // Màn hình giỏ hàng
        }
        composable("favorites") {
            FavouriteScreen()
        }
    }
}


@Composable
fun SearchBar(productViewModel: ProductViewModel) {
    var searchText by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
            },
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            placeholder = { Text("Tìm kiếm tên sản phẩm") },
            leadingIcon = {
                Icon(painterResource(id = R.drawable.search_icon), contentDescription = null)
            },
            trailingIcon = {
                // Nếu đang tìm kiếm, hiển thị icon hủy
                if (isSearching) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        modifier = Modifier.clickable {
                            searchText = "" // Xóa nội dung tìm kiếm
                            isSearching = false
                            productViewModel.fetchProducts() // Gọi lại danh sách đầy đủ
                        }
                    )
                }
            },
            shape = MaterialTheme.shapes.small
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Nút tìm kiếm
        Button(
            onClick = {
                if (searchText.isNotEmpty()) {
                    productViewModel.searchProduct(searchText) // Gọi tìm kiếm khi nhấn nút
                    isSearching = true // Đặt trạng thái đang tìm kiếm
                }
            },
            modifier = Modifier.height(50.dp)
        ) {
            Text("Tìm kiếm")
        }
    }
}


@Composable
fun BannerSlider(imageList: List<Int>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(imageList) { imageRes ->
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(360.dp)
                    .height(180.dp)
            )
        }
    }
}

@Composable
fun CategoryItem(imageRes: Int, title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp) // Khoảng cách xung quanh Card
            .clickable { /* Xử lý sự kiện khi click vào Category */ },
        elevation = 4.dp, // Đổ bóng cho Card
        shape = MaterialTheme.shapes.medium // Bo góc cho Card
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp) // Padding bên trong Card
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
        }
    }
}


@Composable
fun CategoryList(categories: List<Pair<Int, String>>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(imageRes = category.first, title = category.second)
        }
    }
}

@Composable
fun ProductItem(
    imageUrl: String,     // URL của hình ảnh
    rating: Float,        // Đánh giá sản phẩm
    title: String,        // Tên sản phẩm
    price: Double,        // Giá sản phẩm
    description: String,  // Mô tả sản phẩm
    category: String,     // Danh mục sản phẩm
    modifier: Modifier = Modifier,
    onclick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onclick),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(MaterialTheme.colors.surface)
        ) {
            Image(
                painter = rememberImagePainter(data = imageUrl), // Sử dụng Coil để tải hình ảnh từ URL
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$price $",
                    style = MaterialTheme.typography.button.copy(color = Color.Red)
                )
                Text(
                    text = "Cmt: $rating",
                    style = MaterialTheme.typography.button,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ProductListScreen(navController: NavHostController, productViewModel: ProductViewModel) {
    val products = if (productViewModel.searchResults.isNotEmpty()) {
        Log.d("ProductListScreen", "Displaying search results: ${productViewModel.searchResults.joinToString { it.name }}")
        productViewModel.searchResults
    } else {
        Log.d("ProductListScreen", "Displaying full product list: ${productViewModel.productList.joinToString { it.name }}")
        productViewModel.productList
    }
    val isRefreshing = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing.value),
        onRefresh = {
            coroutineScope.launch {
                isRefreshing.value = true
                // Gọi lại hàm để tải lại sản phẩm
                productViewModel.fetchProducts()
                isRefreshing.value = false
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                // Mã hóa URL
                val encodedImageUrl = URLEncoder.encode(product.image_url, StandardCharsets.UTF_8.toString())
                ProductItem(
                    imageUrl = product.image_url,
                    rating = product.ratings,
                    title = product.name,
                    price = product.price,
                    description = product.description,
                    category = product.category,
                    onclick = {
                        navController.navigate(
                            "productDetail/${product.name}/${encodedImageUrl}/${product.price}/${product.description}/${product.category}/${product.ratings}"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, productViewModel: ProductViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        SearchBar(productViewModel= ProductViewModel())
        Spacer(modifier = Modifier.height(16.dp))

        // Phần banner
        val imageList = listOf(
            R.drawable.banner1,
            R.drawable.banner2
        )
        BannerSlider(imageList = imageList)
        Spacer(modifier = Modifier.height(16.dp))

        // Phần danh mục (CategoryList)
        Text(
            text = "Danh Mục",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        val categories = listOf(
            Pair(R.drawable.category1, "Category 1"),
            Pair(R.drawable.category2, "Category 2"),
            Pair(R.drawable.category3, "Category 3")
        )
        CategoryList(categories)
        Spacer(modifier = Modifier.height(16.dp))

        // Phần sản phẩm (ProductListScreen)
        Text(
            text = "Sản Phẩm",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        ProductListScreen(navController = navController, productViewModel = productViewModel)
    }
}


@Composable
fun BottomAppBarContent(navController: NavHostController, currentScreen: String) {
    val xanhduong = Color(0xFF1F479A)
    val nau = Color(0xFF8B4513) // Màu nâu cho mục được chọn
    val xanhNhat = Color(0xFFADD8E6) // Màu xanh nước biển nhạt cho mục không được chọn

    BottomNavigation(
        backgroundColor = xanhduong,
        modifier = Modifier
            .height(100.dp)
            .navigationBarsPadding()
    ) {

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Trang Chủ") },
            label = { Text("Home", textAlign = TextAlign.Center) },
            selected = currentScreen == "home", // Kiểm tra nếu màn hình hiện tại là "home"
            onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selectedContentColor = nau, // Màu nâu cho mục được chọn
            unselectedContentColor = xanhNhat, // Màu xanh nhạt cho mục không được chọn
            modifier = Modifier
                .height(if (currentScreen == "home") 120.dp else 100.dp) // Phóng to khi được chọn
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Yêu Thích") },
            label = { Text("Yêu Thích", textAlign = TextAlign.Center) },
            selected = currentScreen == "favorites", // Kiểm tra nếu màn hình hiện tại là "favorites"
            onClick = {
                navController.navigate("favorites")
            },
            selectedContentColor = nau,
            unselectedContentColor = xanhNhat,
            modifier = Modifier
                .height(if (currentScreen == "favorites") 120.dp else 100.dp)
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Giỏ hàng") },
            label = { Text("Giỏ hàng", textAlign = TextAlign.Center) },
            selected = currentScreen == "cart", // Kiểm tra nếu màn hình hiện tại là "cart"
            onClick = {
                navController.navigate("cart") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selectedContentColor = nau,
            unselectedContentColor = xanhNhat,
            modifier = Modifier
                .height(if (currentScreen == "cart") 120.dp else 100.dp)
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Tài Khoản") },
            label = { Text("Tài Khoản", textAlign = TextAlign.Center) },
            selected = currentScreen == "account", // Kiểm tra nếu màn hình hiện tại là "account"
            onClick = {
                navController.navigate("account")
            },
            selectedContentColor = nau,
            unselectedContentColor = xanhNhat,
            modifier = Modifier
                .height(if (currentScreen == "account") 120.dp else 100.dp)
        )

    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val fakeProductViewModel = ProductViewModel() // Fake ProductViewModel để test preview
    AssgimentTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomAppBarContent(navController, "Home") }
        ) {
            HomeScreen(navController = navController, productViewModel = fakeProductViewModel)
        }
    }
}
