package fpt.tungnqph32251.assgiment.Model

data class Product(
    val name: String,            // Tên sản phẩm
    val image_url: String,       // URL hình ảnh của sản phẩm
    val price: Double,           // Giá của sản phẩm
    val description: String,     // Mô tả sản phẩm
    val category: String,        // Danh mục của sản phẩm
    val ratings: Float,          // Số lượng đánh giá (float vì API trả về số thập phân)
    val createdAt: String,       // Thời gian sản phẩm được tạo
    val updatedAt: String        // Thời gian sản phẩm được cập nhật
)
