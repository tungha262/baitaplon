package com.example.baitaplon.fragments.shopping


import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.baitaplon.R
import com.example.baitaplon.adapter.CartAdapter
import com.example.baitaplon.data.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var totalPriceTextView: TextView
    private lateinit var emptyCartLayout: View
    private lateinit var cartAdapter: CartAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var cartItems: MutableList<Product> = mutableListOf()
    private lateinit var cartClose: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        listView = view.findViewById(R.id.lvCart)
        totalPriceTextView = view.findViewById(R.id.tvTotalPrice)
        emptyCartLayout = view.findViewById(R.id.layout_cart_empty)
        cartClose = view.findViewById(R.id.imageCloseCart)

        sharedPreferences = requireActivity().getSharedPreferences("Cart", android.content.Context.MODE_PRIVATE)
        loadCartItems()
        setupListView()
        updateTotalPrice()


        cartClose.setOnClickListener {
            requireActivity().onBackPressed()
        }

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
