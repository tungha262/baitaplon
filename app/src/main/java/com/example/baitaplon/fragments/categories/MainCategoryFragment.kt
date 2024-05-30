package com.example.baitaplon.fragments.categories

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.activity.ProductDetail
import com.example.baitaplon.adapter.ProductAdapter
import com.example.baitaplon.data.Product
import com.example.baitaplon.databinding.FragmentMainCategoryBinding
import com.example.baitaplon.productController.ProductManager

class MainCategoryFragment : Fragment(R.layout.fragment_main_category){
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var adt: ProductAdapter
    private lateinit var productManager: ProductManager
    private lateinit var filteredProduct: ArrayList<Product>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = binding.recyclerViewProducts1
        val layoutManager = GridLayoutManager(requireContext(), 1)
        filteredProduct = ArrayList()
        adt = ProductAdapter(filteredProduct) { product ->
            showProductDetail(product)
        }
        recyclerView.setAdapter(adt)
        recyclerView.layoutManager = layoutManager
        productManager = ProductManager(requireContext())
        loadProducts()
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                loadProducts(searchQuery = s.toString())
            }
        })
    }
    private fun loadProducts(searchQuery: String = "") {
        productManager.getProducts(object : ProductManager.ProductCallback {
            override fun onProductsLoaded(products: List<Product>) {
                hideLoading()
                Log.d("MainCategoryFragment", "Loaded ${products.size} products")
                val filteredList = products.filter { 
                    (searchQuery.isEmpty() || it.productName!!.contains(searchQuery, ignoreCase = true))
                }
                filteredProduct.clear()
                filteredProduct.addAll(filteredList)
                adt.notifyDataSetChanged()
            }
            override fun onLoadError(error: VolleyError) {
                showLoading()
                Log.e("MainCategoryFragment", "Error loading products: ${error.message}")
            }
        })
    }
    private fun showProductDetail(product: Product) {
        val intent = Intent(requireContext(), ProductDetail::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.GONE
    }
    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }


}

