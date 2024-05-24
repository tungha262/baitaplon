package com.example.baitaplon.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.baitaplon.R
import com.example.baitaplon.data.Product
import com.squareup.picasso.Picasso

class ProductDetail : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var productImage : ImageView
    private lateinit var productPrice : TextView
    private lateinit var productName : TextView
    private lateinit var productYearOfManufacture : TextView
    private lateinit var productDescription : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val product: Product? = intent.getParcelableExtra("product")
        productImage = findViewById(R.id.product_Image)
        productPrice = findViewById(R.id.product_price)
        productName = findViewById(R.id.product_name)
        productYearOfManufacture = findViewById(R.id.product_year_of_manufacture)
        productDescription = findViewById(R.id.product_description)
        product?.let {
            displayProductDetails(it)
        }
    }

    private fun displayProductDetails(product: Product) {
        Picasso.get().load(product.imageLink).into(productImage)
        productName.text = product.productName
        val formatPrice = product.price?.formatPrice()
        productPrice.text = formatPrice
        productYearOfManufacture.text = "Năm sản xuất: ${product.yearOfManufacture}"
        productDescription.text = "${product.description}\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus vitae lacus vel velit gravida tristique. Fusce facilisis, ipsum ut placerat viverra, metus justo tincidunt justo, non scelerisque sem ex a libero. Duis pharetra, magna in auctor vehicula, libero odio volutpat urna, sit amet cursus nisi velit non libero. Quisque tempor dui sit amet libero tincidunt, sed congue neque efficitur. Nam posuere vestibulum elit, id lobortis neque vestibulum in. Integer auctor tortor id suscipit scelerisque. Vivamus eget volutpat dui. Pellentesque sit amet orci ut purus consequat eleifend.\n" +
                "\n" +
                "Sed vestibulum, velit non malesuada tincidunt, erat risus auctor est, nec vestibulum magna lorem eget libero. Curabitur sit amet tortor nisl. Fusce id nibh et elit interdum ultricies. Ut auctor neque ut leo tempus, in suscipit erat porttitor. Mauris at ullamcorper est. Nullam fringilla vehicula orci, ac vehicula eros tincidunt sed. Aenean tristique, erat sed cursus tincidunt, justo leo facilisis orci, sit amet aliquam dui magna id augue.\n"
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