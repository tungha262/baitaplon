package com.example.baitaplon.productController
import android.content.Context
import com.android.volley.VolleyError
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.data.Product
import org.json.JSONArray

class ProductManager(private val context: Context) {

    fun getProducts(callback: ProductCallback) {
        val serverService = ServerService(context)

        // Callback for handling server response
        val serverCallbackArray = object : ServerService.ServerCallbackArray {
            override fun onSuccess(response: JSONArray) {
                val productList = parseProducts(response)
                callback.onProductsLoaded(productList)
            }

            override fun onError(error: VolleyError) {
                callback.onLoadError(error)
            }
        }

        // Request products from server
        serverService.getProducts(serverCallbackArray)
    }

    // Parse JSON response to list of Product objects
    private fun parseProducts(jsonArray: JSONArray): List<Product> {
        val productList = mutableListOf<Product>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val product = Product(
                id = jsonObject.getInt("id"),
                imageLink = jsonObject.getString("image"),
                productName = jsonObject.getString("productName"),
                price = jsonObject.getInt("price"),
                brand = jsonObject.getString("brand"),
                yearOfManufacture = jsonObject.getInt("yearOfManufacture"),
                description = jsonObject.getString("description")
            )
            productList.add(product)
        }

        return productList
    }

    // Callback interface for loading products
    interface ProductCallback {
        fun onProductsLoaded(products: List<Product>)
        fun onLoadError(error: VolleyError)
    }
}
