package com.example.baitaplon.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.data.Product
import com.example.baitaplon.productController.UserManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ProductDetail : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var productImage: ImageView
    private lateinit var productPrice: TextView
    private lateinit var productName: TextView
    private lateinit var productYearOfManufacture: TextView
    private lateinit var productDescription: TextView
    private lateinit var btnAddToCart: Button
    private lateinit var btnClose: ImageView
    private lateinit var userManager: UserManager
    private lateinit var serverService: ServerService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userManager = UserManager
        serverService = ServerService(this)
        val product: Product? = intent.getParcelableExtra("product")
        productImage = findViewById(R.id.product_Image)
        productPrice = findViewById(R.id.product_price)
        productName = findViewById(R.id.product_name)
        productYearOfManufacture = findViewById(R.id.product_year_of_manufacture)
        productDescription = findViewById(R.id.product_description)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)
        btnClose = findViewById(R.id.btn_close)

        btnClose.setOnClickListener {
            finish()
        }
        product?.let {
            displayProductDetails(it)
            btnAddToCart.setOnClickListener {
                // TODO: Add product to cart
                addToCart(product)
            }
        }
    }

    private fun displayProductDetails(product: Product) {
        Picasso.get().load(product.imageLink).into(productImage)
        productName.text = product.productName
        val formatPrice = product.price?.formatPrice()
        productPrice.text = formatPrice
        productYearOfManufacture.text = "Năm sản xuất: ${product.yearOfManufacture}"
        productDescription.text =
            "${product.description}\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus vitae lacus vel velit gravida tristique. Fusce facilisis, ipsum ut placerat viverra, metus justo tincidunt justo, non scelerisque sem ex a libero. Duis pharetra, magna in auctor vehicula, libero odio volutpat urna, sit amet cursus nisi velit non libero. Quisque tempor dui sit amet libero tincidunt, sed congue neque efficitur. Nam posuere vestibulum elit, id lobortis neque vestibulum in. Integer auctor tortor id suscipit scelerisque. Vivamus eget volutpat dui. Pellentesque sit amet orci ut purus consequat eleifend.\n"
    }

    private fun addToCart(product: Product) {

        val currentUser = userManager.getCurrentUser()
        val email = currentUser?.email ?: ""
        val token = "" // Token mặc định, bạn có thể thay đổi nếu cần
        Log.d("ProductID", product.id.toString())
        Log.d("Email", email)
        serverService.addToCart(product.id ?: 0, email, token, object : ServerService.ServerCallback {
            override fun onSuccess(response: JSONObject) {
                // Xử lý khi thêm vào giỏ hàng thành công
                Toast.makeText(this@ProductDetail, "Add to cart successfully", Toast.LENGTH_SHORT).show()
                Log.d("Add to Cart", "Success: $response")
            }

            override fun onError(error: VolleyError) {
                // Xử lý khi thất bại
                Log.e("Add to Cart", "Error: ${error.toString()}")
                Toast.makeText(this@ProductDetail, "Failed to add to cart", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

private fun Int?.formatPrice(): String {
    val price = this.toString()
    val stringBuilder = StringBuilder()
    val n = price.length
    var count = 0
    for (i in n - 1 downTo 0) {
        stringBuilder.append(price[i])
        count++
        if (count == 3 && i != 0) {
            stringBuilder.append('.')
            count = 0
        }
    }
    return stringBuilder.reverse().toString()
}