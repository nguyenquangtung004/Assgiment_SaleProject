package fpt.tungnqph32251.assgiment.Screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import fpt.tungnqph32251.assgiment.R
import fpt.tungnqph32251.assgiment.Screen.ui.theme.AssgimentTheme
import kotlinx.coroutines.delay

class Welcome : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssgimentTheme {
                // Gọi hàm WelcomeScreen để hiển thị hoạt ảnh và chuyển sang Login sau 3 giây
                WelcomeScreen { navigateToLogin() }
            }
        }
    }

    // Hàm để chuyển sang màn hình Login
    private fun navigateToLogin() {
        // Chuyển sang màn hình Login
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Kết thúc màn hình Welcome
    }
}

// Hàm Composable để hiển thị hoạt ảnh và văn bản "Welcome"
@Composable
fun WelcomeScreen(onTimeout: () -> Unit) {
    // Sử dụng LaunchedEffect để thêm delay sau 3 giây và chuyển màn hình
    LaunchedEffect(Unit) {
        delay(3000) // Chờ 3 giây
        onTimeout() // Gọi hàm chuyển hướng sau khi hết thời gian
    }

    // Gọi hàm để hiển thị hoạt ảnh Lottie
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Thêm padding xung quanh giao diện
        verticalArrangement = Arrangement.Center, // Căn giữa theo chiều dọc
        horizontalAlignment = Alignment.CenterHorizontally // Căn giữa theo chiều ngang
    ) {
        // Giới hạn kích thước của LottieAnimation
        AnhDong(modifier = Modifier.size(200.dp)) // Kích thước 200.dp cho hoạt ảnh

        Spacer(modifier = Modifier.height(16.dp)) // Thêm khoảng cách giữa hoạt ảnh và text

        TextWelcome() // Hiển thị văn bản Welcome Shopping
    }
}

// Hàm Composable để hiển thị văn bản "Welcome Shopping"
@Composable
fun TextWelcome() {
    Text(
        text = "Welcome Shopping",
        modifier = Modifier.padding(top = 16.dp), // Thêm khoảng cách phía trên
        style = MaterialTheme.typography.headlineMedium // Sử dụng style headlineMedium cho văn bản
    )
}

// Hàm Composable để hiển thị Lottie animation
@Composable
fun AnhDong(modifier: Modifier = Modifier) {
    // Load file JSON cho animation từ thư mục raw
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.welcomeapp))

    // Điều khiển trạng thái của animation (lặp lại, tốc độ, ...)
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // Hiển thị Lottie animation trong UI với kích thước giới hạn
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier // Sử dụng kích thước được truyền vào
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    // Hiển thị preview của WelcomeScreen
    AssgimentTheme {
        WelcomeScreen(onTimeout = {})
    }
}
