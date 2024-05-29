package com.example.baitaplon.fragments.shopping

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.activity.ShoppingActivity
import com.example.baitaplon.databinding.FragmentCheckoutBinding
import com.example.baitaplon.productController.UserManager
import org.json.JSONObject
import java.util.UUID


class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var serverService: ServerService
    private lateinit var userManager: UserManager
    private var totalPrice: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serverService = ServerService(requireContext())
        userManager = UserManager

        totalPrice = (arguments?.getInt("totalPrice") ?: 0).toString()
        Log.d("Check", "totalPrice: $totalPrice")
        binding.tvCheckoutPrice.text = totalPrice
        binding.imageAddressClose.setOnClickListener {
            findNavController().popBackStack(R.id.cartFragment, false)
        }

        binding.buttonSave.setOnClickListener{
            createOrder()
        }
    }
    private fun createOrder() {
        val orderLabel = UUID.randomUUID().toString()
        val fullName = binding.edFullName.text.toString()
        val address = binding.edAddress.text.toString()
        val phoneNumber = binding.edPhone.text.toString()
        val email = userManager.getCurrentUser()?.email
        // Validate input fields
        if (email != null) {
            if (email.isEmpty() || fullName.isEmpty() || address.isEmpty()|| phoneNumber.isEmpty()) {
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
            put("totalAmount", totalPrice?.toInt())
            put("token","")
        }

        serverService.createOrder(orderData, object : ServerService.ServerCallback {
            override fun onSuccess(response: JSONObject) {
                serverService.clearShoppingCart(email!!, object : ServerService.ServerCallback {
                    override fun onSuccess(response: JSONObject) {
                        Log.d("ClearShoppingCart", "Thanh cong: $response")
                    }

                    override fun onError(error: VolleyError) {
                        Log.e("ClearShoppingCart", "Loi: ${error.message}")
                    }
                })
                Toast.makeText(requireContext(), "Order created successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), ShoppingActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            override fun onError(error: VolleyError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}