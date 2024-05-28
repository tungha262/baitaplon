package com.example.baitaplon.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.databinding.FragmentCheckoutBinding
import com.example.baitaplon.productController.UserManager
import org.json.JSONObject


class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var serverService: ServerService
    private lateinit var userManager: UserManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Thực hiện data binding
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)

        // Trả về root view đã được liên kết
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serverService = ServerService(requireContext())
        userManager = UserManager

        val totalPrice = arguments?.getString("totalPrice") ?: ""
        binding.tvCheckoutPrice.text = totalPrice
        // Add click listener to the close image
        binding.imageAddressClose.setOnClickListener {
            // Navigate back to the cart fragment when close image is clicked
            findNavController().popBackStack(R.id.cartFragment, false)
        }

        binding.buttonSave.setOnClickListener{
            createOrder()
        }
    }
    private fun createOrder() {
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
            put("email", email)
            put("fullName", fullName)
            put("address", address)
            put("phoneNumber", phoneNumber)
            put("token","")
        }

        // Call the server to create the order
        serverService.createOrder(orderData, object : ServerService.ServerCallback {
            override fun onSuccess(response: JSONObject) {
                // Handle success response
                // For example, show a success message
                Toast.makeText(requireContext(), "Order created successfully", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: VolleyError) {
                // Handle error
                // For example, show an error message
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}