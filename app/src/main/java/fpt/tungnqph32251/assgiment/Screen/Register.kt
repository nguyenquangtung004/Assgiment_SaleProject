package fpt.tungnqph32251.assgiment.Screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import fpt.tungnqph32251.assgiment.R
import fpt.tungnqph32251.assgiment.Screen.ui.theme.AssgimentTheme
import fpt.tungnqph32251.assgiment.viewmodel.AuthViewModel

class Register : ComponentActivity() {
    private val authViewModel = AuthViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssgimentTheme {
                RegisterScreen(onBackToLogin = { navigateToLogin() }, authViewModel)
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBackToLogin: () -> Unit, authViewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Thêm VideoView làm nền
        AndroidView(
            factory = {
                VideoView(it).apply {
                    val videoUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.backgroundv1)
                    setVideoURI(videoUri)
                    setOnPreparedListener { mp ->
                        mp.isLooping = true
                        start()
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Lớp phủ tối để dễ nhìn thấy nội dung
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // Thẻ đăng ký với hiệu ứng đổ bóng
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center)
                .zIndex(1f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation =0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Đăng Ký",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
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
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("Tên tài khoản") },
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
                Text(
                    text = "Mật khẩu ",
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
                    onValueChange = { password = it },
                    placeholder = { Text("Mật khẩu") },
                    visualTransformation = PasswordVisualTransformation(),
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
                Text(
                    text = "Nhập lại mật khẩu ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    color = Color.Black
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Nhập lại mật khẩu") },
                    visualTransformation = PasswordVisualTransformation(),
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
                Text(
                    text = "Số điện thoại ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    color = Color.Black
                )
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    placeholder = { Text("Nhập số điện thoại") },
                    visualTransformation = PasswordVisualTransformation(),
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
                Text(
                    text = "Email ",
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
                    onValueChange = { email = it },
                    placeholder = { Text("Nhập email") },
                    visualTransformation = PasswordVisualTransformation(),
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
                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                }

                // Nút đăng ký và trở lại
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (password == confirmPassword) {
                                authViewModel.registerUser(username, username, email, password,
                                    onSuccess = {
                                        Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                                        onBackToLogin() // Chuyển sang màn hình đăng nhập sau khi đăng ký thành công
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, "Lỗi: $error", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            } else {
                                Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Màu nền của button
                            contentColor = Color.White // Màu của chữ và icon bên trong button
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .padding(end = 8.dp)
                    ) {
                        Text(text = "Đăng ký", fontSize = 18.sp)
                    }

                    Button(
                        onClick = { onBackToLogin() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Màu nền của button
                            contentColor = Color.White // Màu của chữ và icon bên trong button
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .padding(start = 8.dp)

                    ) {
                        Text(text = "Trở lại", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    // Tạo một mock AuthViewModel không có chức năng thực
    val mockAuthViewModel = AuthViewModel()

    // Gọi màn hình RegisterScreen với các giá trị giả
    RegisterScreen(
        onBackToLogin = {}, // Hành động giả khi nhấn "Trở lại"
        authViewModel = mockAuthViewModel
    )
}
