package fpt.tungnqph32251.assgiment.Screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import fpt.tungnqph32251.assgiment.R
import fpt.tungnqph32251.assgiment.Screen.ui.theme.AssgimentTheme
import fpt.tungnqph32251.assgiment.viewmodel.AuthViewModel

class Login : ComponentActivity() {
    private val authViewModel = AuthViewModel() // Khởi tạo ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Kích hoạt chế độ toàn màn hình
        setContent {
            AssgimentTheme {
                LoginScreen(
                    onLoginClick = { navigateToHome() }, // Hàm điều hướng sang màn hình Home khi đăng nhập
                    onRegisterClick = { navigateToRegister() },
                    authViewModel = authViewModel
                )
            }
        }
    }

    // Hàm điều hướng sang màn hình Home
    private fun navigateToHome() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish() // Đóng màn hình hiện tại sau khi điều hướng
    }

    // Hàm điều hướng sang màn hình Đăng ký
    private fun navigateToRegister() {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginClick: () -> Unit, onRegisterClick: () -> Unit, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") } // Lưu trữ và khôi phục trạng thái cho email
    var password by rememberSaveable { mutableStateOf("") } // Lưu trữ và khôi phục trạng thái cho mật khẩu
    var rememberMe by rememberSaveable { mutableStateOf(false) } // Lưu trữ trạng thái checkbox "Ghi nhớ mật khẩu"
    var passwordVisible by rememberSaveable { mutableStateOf(false) } // Trạng thái bật/tắt hiển thị mật khẩu
    Box(
        modifier = Modifier
            .fillMaxSize() // Chiếm toàn bộ màn hình
    ) {
        // Sử dụng VideoView làm nền cho giao diện
        AndroidView(
            factory = {
                VideoView(it).apply {
                    val videoUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.backgroundv1) // Tải video từ thư mục res/raw
                    setVideoURI(videoUri)
                    setOnPreparedListener { mp ->
                        mp.isLooping = true // Lặp lại video khi phát hết
                        mp.setScreenOnWhilePlaying(true) // Giữ màn hình không tắt khi video phát
                        start() // Tự động phát video
                    }
                }
            },
            modifier = Modifier.fillMaxSize() // VideoView chiếm toàn bộ màn hình
        )

        // Lớp phủ tối trên video để làm rõ các thành phần UI
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // Làm tối video một chút
        )

        // Các thành phần UI bên trong Card với nền trong suốt
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Cách lề xung quanh UI
            contentAlignment = Alignment.Center // Canh giữa các thành phần UI
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), // Cách lề của Card
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.5f) // Nền Card trong suốt
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Không có bóng đổ cho Card
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp), // Padding bên trong Card
                    horizontalAlignment = Alignment.CenterHorizontally, // Căn giữa các thành phần trong chiều ngang
                    verticalArrangement = Arrangement.Center // Căn giữa các thành phần trong chiều dọc
                ) {
                    Text(
                        text = "Đăng Nhập", // Tiêu đề đăng nhập
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp) // Khoảng cách từ trên xuống tiêu đề
                    )

                    Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa tiêu đề và các trường nhập liệu

                    // Trường nhập tài khoản
                    Text(
                        text = "Tài khoản",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it }, // Cập nhật email
                        placeholder = { Text("Nhập email của bạn") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black, // Màu viền khi được chọn
                            unfocusedBorderColor = Color.Black, // Màu viền khi không được chọn
                            cursorColor = Color.Black // Màu của con trỏ
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa hai trường nhập liệu

                    // Trường nhập mật khẩu
                    Text(
                        text = "Mật khẩu",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it }, // Cập nhật mật khẩu
                        placeholder = { Text("Nhập mật khẩu") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Hiển thị hoặc ẩn mật khẩu
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Bàn phím kiểu nhập mật khẩu
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                        ,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black, // Màu viền khi được chọn
                            unfocusedBorderColor = Color.Black, // Màu viền khi không được chọn
                            cursorColor = Color.Black // Màu của con trỏ
                        )
                        ,
                        trailingIcon = { // Biểu tượng mắt để bật/tắt hiển thị mật khẩu
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            IconButton(onClick = {
                                passwordVisible = !passwordVisible // Đổi trạng thái hiển thị mật khẩu
                            }) {
                                Icon(imageVector = image, contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiển thị mật khẩu")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách trước phần checkbox và quên mật khẩu

                    // Quên mật khẩu và ghi nhớ mật khẩu
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it } // Xử lý thay đổi trạng thái checkbox
                            )
                            Text(
                                text = "Ghi nhớ mật khẩu",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Quên mật khẩu?",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { /* TODO: Xử lý quên mật khẩu */ }, // Xử lý sự kiện quên mật khẩu
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp)) // Khoảng cách trước nút đăng nhập và đăng ký

                    // Nút đăng nhập và đăng ký
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                authViewModel.loginUser(email, password,
                                    onSuccess = {
                                        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                        onLoginClick() // Chuyển sang màn hình Home sau khi đăng nhập thành công
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }, // Xử lý sự kiện khi nhấn "Đăng nhập"
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, // Màu nền của button
                                contentColor = Color.White // Màu của chữ và icon bên trong button
                            )
                            ,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(end = 8.dp)
                        ) {
                            Text(text = "Đăng nhập", fontSize = 18.sp)
                        }
                        Button(
                            onClick = { onRegisterClick() }, // Xử lý sự kiện khi nhấn "Đăng ký"
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, // Màu nền của button
                                contentColor = Color.White // Màu của chữ và icon bên trong button
                            )
                            ,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(start = 8.dp)
                        ) {
                            Text(text = "Đăng ký", fontSize = 18.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val mockAuthViewModel = AuthViewModel()
    AssgimentTheme {
        LoginScreen(onLoginClick = {}, onRegisterClick = {}, authViewModel = mockAuthViewModel) // Xem trước màn hình đăng nhập
    }
}
