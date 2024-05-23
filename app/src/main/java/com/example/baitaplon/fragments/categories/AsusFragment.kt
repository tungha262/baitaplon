package com.example.baitaplon.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.adapter.ProductAdapter
import com.example.baitaplon.data.Product
import com.example.baitaplon.databinding.FragmentMainCategoryBinding
import com.example.baitaplon.productController.ProductManager

class AsusFragment : Fragment(R.layout.fragment_main_category){
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
        adt = ProductAdapter(filteredProduct)
        recyclerView.setAdapter(adt)
        recyclerView.layoutManager = layoutManager
        productManager = ProductManager(requireContext())
        loadProducts("Asus")
    }
    private fun loadProducts(brand : String) {
        productManager.getProducts(object : ProductManager.ProductCallback {
            override fun onProductsLoaded(products: List<Product>) {
                hideLoading()
                Log.d("MainCategoryFragment", "Loaded ${products.size} products")
                val finalList = products.filter { it.brand.equals(brand, ignoreCase = true) }
                filteredProduct.clear()
                filteredProduct.addAll(finalList)
                adt.notifyDataSetChanged()
            }

            override fun onLoadError(error: VolleyError) {
                showLoading()
                Log.e("MainCategoryFragment", "Error loading products: ${error.message}")
            }
        })
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.GONE
    }
    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }


}