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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.adapter.CartAdapter
import com.example.baitaplon.data.Product
import com.google.gson.Gson
import com.example.baitaplon.productController.UserManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartFragment : Fragment(), CartAdapter.CartListener {

    private lateinit var listView: ListView
    private lateinit var totalPriceTextView: TextView
    private lateinit var emptyCartLayout: View
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var cartItems: MutableList<Product> = mutableListOf()
    private lateinit var cartClose: ImageView
    private lateinit var userManager: UserManager
    private lateinit var serverService: ServerService

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

        view.findViewById<View>(R.id.buttonCheckout).setOnClickListener{
            onCheckoutButtonClick()
        }
        return view
    }

    private fun setupListView() {
        cartAdapter = CartAdapter(requireContext(), cartItems)
        cartAdapter.setCartListener(this) // Đăng ký listener
        listView.adapter = cartAdapter
    }

    override fun onProductRemoved(product: Product) {
        // Xử lý khi sản phẩm bị xoá
        product.quantity = product.quantity!! - 1
        if (product.quantity == 0) {
            cartItems.remove(product)
        }
        updateTotalPrice()
        if (cartItems.isEmpty()) {
            emptyCartLayout.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            emptyCartLayout.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }

        cartAdapter.notifyDataSetChanged()
    }

    override fun onProductQuantityChanged(product: Product, quantity: Int) {
        // Xử lý khi số lượng sản phẩm thay đổi
        product.quantity = quantity
        Log.d("Quantity", quantity.toString())
        updateTotalPrice()
        addToCart(product)
        cartAdapter.notifyDataSetChanged()
    }

    private fun updateTotalPrice() {
        var totalPrice = 0
        for (product in cartItems) {
            totalPrice += product.price!! * product.quantity!!
        }
        totalPriceTextView.text = totalPrice.formatPrice()
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
                Log.d("Add to Cart", "Success: $response")
            }

            override fun onError(error: VolleyError) {
                // Xử lý khi thất bại
                Log.e("Add to Cart", "Error: ${error.toString()}")
            }
        })
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
                    description = jsonObject.getString("description"),
                    quantity = jsonObject.getInt("quantity")
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
            Log.e("CartFragment", "JSON parsing error: ${e.message}")
        }
    }
    private fun onCheckoutButtonClick() {
        val totalPrice = totalPriceTextView.text.toString()
        val bundle = Bundle()
        bundle.putString("totalPrice", totalPrice)
        findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment, bundle)
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
