package com.example.baitaplon.fragments.shopping


import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.adapter.CartAdapter
import com.example.baitaplon.data.Product
import com.google.gson.Gson
import com.example.baitaplon.productController.UserManager
import org.json.JSONArray
import org.json.JSONException

class CartFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var totalPriceTextView: TextView
    private lateinit var emptyCartLayout: View
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var cartItems: MutableList<Product> = mutableListOf()
    private lateinit var cartClose: ImageView
    private lateinit var userManager : UserManager
    private lateinit var serverService : ServerService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        userManager = UserManager
        serverService = ServerService(requireContext())

        val progressBar = view.findViewById<ProgressBar>(R.id.progressbarCart)
        progressBar.visibility = View.VISIBLE

        listView = view.findViewById(R.id.lvCart)
        totalPriceTextView = view.findViewById(R.id.tvTotalPrice)
        emptyCartLayout = view.findViewById(R.id.layout_cart_empty)
        cartClose = view.findViewById(R.id.imageCloseCart)

        sharedPreferences = requireActivity().getSharedPreferences("Cart", android.content.Context.MODE_PRIVATE)
//        loadCartItems()
        setupListView()
        updateTotalPrice()


        cartClose.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val currentUser = userManager.getCurrentUser()
        val email = currentUser?.email ?: ""

        // Gọi phương thức để lấy dữ liệu giỏ hàng từ server
        serverService.getProductByEmail(email, object : ServerService.ServerCallbackArray {
            override fun onSuccess(response: JSONArray) {
                // Xử lý khi nhận được dữ liệu giỏ hàng từ server
                handleCartItemsResponse(response)
                progressBar.visibility = View.GONE
            }

            override fun onError(error: VolleyError) {
                // Xử lý khi có lỗi xảy ra
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed to load cart items", Toast.LENGTH_SHORT).show()
                Log.e("CartFragment", "Error: ${error.toString()}")
            }
        })
        return view
    }

    private fun loadCartItems() {
        val allEntries = sharedPreferences.all
        cartItems.clear()
        for ((_, value) in allEntries) {
            val product = Gson().fromJson(value as String, Product::class.java)
            cartItems.add(product)
        }
        if (cartItems.isEmpty()) {
            emptyCartLayout.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            emptyCartLayout.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
    }

    private fun setupListView() {
        cartAdapter = CartAdapter(requireContext(), cartItems)
        listView.adapter = cartAdapter
    }

    private fun updateTotalPrice() {
        val totalPrice = cartItems.sumBy { it.price ?: 0 }
        totalPriceTextView.text = totalPrice.formatPrice()
    }


    private fun handleCartItemsResponse(response: JSONArray) {
        try {
            // Xóa các mục hiện có trong giỏ hàng
            cartItems.clear()
            // Phân tích và chuyển đổi dữ liệu JSON thành danh sách các sản phẩm
            for (i in 0 until response.length()) {
                val jsonObject = response.getJSONObject(i)
                val product = Product(
                    id = jsonObject.getInt("id"),
                    imageLink = jsonObject.getString("image"),
                    productName = jsonObject.getString("productName"),
                    price = jsonObject.getInt("price"),
                    brand = jsonObject.getString("brand"),
                    yearOfManufacture = jsonObject.getInt("yearOfManufacture"),
                    description = jsonObject.getString("description")
                )
                cartItems.add(product)
            }
            // Cập nhật giao diện người dùng
            if (cartItems.isEmpty()) {
                emptyCartLayout.visibility = View.VISIBLE
                listView.visibility = View.GONE
            } else {
                emptyCartLayout.visibility = View.GONE
                listView.visibility = View.VISIBLE
            }
            cartAdapter.notifyDataSetChanged() // Cập nhật ListView
            updateTotalPrice() // Cập nhật tổng giá
        } catch (e: JSONException) {
            // Xử lý ngoại lệ nếu có lỗi khi phân tích dữ liệu JSON
//            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show()
            Log.e("CartFragment", "JSON parsing error: ${e.message}")
        }
    }

    private fun Int.formatPrice(): String {
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
}
