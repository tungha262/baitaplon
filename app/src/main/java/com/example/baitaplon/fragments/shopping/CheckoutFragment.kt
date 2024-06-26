package com.example.baitaplon.fragments.shopping

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.activity.ShoppingActivity
import com.example.baitaplon.databinding.FragmentCheckoutBinding
import com.example.baitaplon.productController.UserManager
import com.jakewharton.threetenabp.AndroidThreeTen
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var serverService: ServerService
    private lateinit var userManager: UserManager
    private var totalPrice: String? = null
    private var totalPriceTmp: Int? = null
    private var imageLink: String? = null
    private var orderLabel: String? = null

    companion object {
        private const val PAYMENT_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        AndroidThreeTen.init(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serverService = ServerService(requireContext())
        userManager = UserManager

        totalPrice = (arguments?.getInt("totalPrice") ?: 0).formatPrice()
        totalPriceTmp = arguments?.getInt("totalPrice")
        imageLink = arguments?.getString("imageLink")
        Log.d("Check", "imageLink: $imageLink")
        Log.d("Check", "totalPrice: $totalPrice")
        binding.tvCheckoutPrice.text = totalPrice
        binding.imageAddressClose.setOnClickListener {
            findNavController().popBackStack(R.id.cartFragment, false)
        }

        binding.buttonSave.setOnClickListener {
            createOrder()
        }
    }

    private fun createOrder() {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
        orderLabel = currentDateTime.format(formatter)
        val fullName = binding.edFullName.text.toString()
        val address = binding.edAddress.text.toString()
        val phoneNumber = binding.edPhone.text.toString()
        val email = userManager.getCurrentUser()?.email

        // Validate input fields
        if (email != null) {
            if (email.isEmpty() || fullName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
                // Show an error message if any field is empty
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Create a JSON object with the order data
        val orderData = JSONObject().apply {
            put("orderLabel", orderLabel)
            put("email", email)
            put("fullName", fullName)
            put("address", address)
            put("phoneNumber", phoneNumber)
            put("totalAmount", totalPriceTmp)
            put("token", "")
            put("firstOrderImage", imageLink)
        }
        Log.d("OrderData", "Order data: $orderData")
        serverService.createOrder(orderData, object : ServerService.ServerCallback {
            override fun onSuccess(response: JSONObject) {
                if (isAdded) {
                    serverService.clearShoppingCart(email!!, object : ServerService.ServerCallback {
                        override fun onSuccess(response: JSONObject) {
                            Log.d("ClearShoppingCart", "Thanh cong: $response")
                        }

                        override fun onError(error: VolleyError) {
                            Log.e("ClearShoppingCart", "Loi: ${error.message}")
                        }
                    })

                    serverService.paymentProcess(totalPriceTmp!!, orderLabel!!, object : ServerService.ServerCallback {
                        override fun onSuccess(response: JSONObject) {
                            val paymentUrl = response.getString("paymentUrl")
                            Log.d("PaymentUrl", "Payment URL: $paymentUrl")
                            openPaymentWebView(paymentUrl)
                        }

                        override fun onError(error: VolleyError) {
                            if (isAdded) {
                                Log.e("PaymentUrl", "Error: ${error.message}")
                            }
                        }
                    })
                }
            }

            override fun onError(error: VolleyError) {
                if (isAdded) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun openPaymentWebView(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        try {
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
        } catch (e: ActivityNotFoundException) {
            // Xử lý khi không tìm thấy trình duyệt web
            Toast.makeText(requireContext(), "No web browser found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Kiểm tra trạng thái đơn hàng khi người dùng quay lại ứng dụng
        orderLabel?.let {
            checkOrderStatus(it)
        }
    }

    private fun checkOrderStatus(orderLabel: String) {
        val email = userManager.getCurrentUser()?.email
        if (email != null) {
            serverService.getOrderByLabel(orderLabel, object : ServerService.ServerCallback {
                override fun onSuccess(response: JSONObject) {
                    val token = response.getString("token")
                    if (token.isNotEmpty()) {
                        // Thanh toán thành công
                        Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_SHORT).show()
                    } else {
                        // Thanh toán thất bại hoặc chưa thực hiện
                        Toast.makeText(requireContext(), "Payment failed or pending", Toast.LENGTH_SHORT).show()
                    }
                    val intent = Intent(requireContext(), ShoppingActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                override fun onError(error: VolleyError) {
                    Log.e("CheckOrderStatus", "Error: ${error.message}")
                }
            })
        }
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
