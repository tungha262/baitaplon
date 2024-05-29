package com.example.baitaplon.Server

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ServerService(context: Context) {
    private val TAG = "ServerService"
    private val API_URL = "https://serveruddd.onrender.com/api/"
    private val insertAccount = "insertaccount"
    private val getProduct = "product"
    private val getAccountByEmailStr = "getAccountByEmail"
    private val addToCartEndpoint = "add_to_shopping_cart"
    private val getProductByEmail = "getProductByEmail"
    private val removeProduct = "removeProductByID"
    private val createOrderURL = "insertOrder"
    private val updateOrderURL = "updateOrderByID"
    private val clearShoppingCart = "clearShoppingCartByEmail"
    private val getOrderByEmail = "getOrderByEmail"
    private val payment = "payment"
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    fun createAccount(postData: JSONObject, callback: ServerCallback){
        val url = API_URL + insertAccount
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, postData,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    fun getProducts(callback: ServerCallbackArray) {
        val url = API_URL + getProduct
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }
        requestQueue.add(jsonArrayRequest)
    }


    //Lay du lieu nguoi dung bang email
    fun getAccountByEmail(email: String, callback: ServerCallback) {
        val url = "$API_URL$getAccountByEmailStr/$email"
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    fun addToCart(productId: Int, email: String, token: String, callback: ServerCallback) {
        val url = API_URL + addToCartEndpoint

        // Tạo đối tượng JSONObject chứa dữ liệu cần gửi đi
        val postData = JSONObject().apply {
            put("productID", productId)
            put("email", email)
            put("token", token)
        }

        // Tạo request
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, postData,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }

        // Thêm request vào hàng đợi
        requestQueue.add(jsonObjectRequest)
    }

    fun getProductByEmail(email: String, callback: ServerCallbackArray) {
        val url = "$API_URL$getProductByEmail/$email"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }
        requestQueue.add(jsonArrayRequest)
    }

    fun removeProductByID(productId: Int, callback: ServerCallback) {
        val url = "$API_URL$removeProduct/$productId"

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.DELETE, url, null,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    fun createOrder(postData: JSONObject, callback: ServerCallback){
        val url = API_URL + createOrderURL
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, postData,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    fun updateOrder(token: String, orderID: Int, callback: ServerCallback){
        val url = "$API_URL$updateOrderURL/$orderID"
        val postData = JSONObject().apply {
            put("token", token)
        }

        // Tạo request
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.PUT, url, postData,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }

        // Thêm request vào hàng đợi
        requestQueue.add(jsonObjectRequest)
    }

    fun clearShoppingCart(email: String, callback: ServerCallback){
        val url = "$API_URL${clearShoppingCart}/$email"
        val postData = JSONObject().apply {
            put("email", email)
        }
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.DELETE, url, postData,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }

        requestQueue.add(jsonObjectRequest)
    }


    fun getOrderByEmail(email: String, callback: ServerCallbackArray){
        val url = "$API_URL$getOrderByEmail/$email"
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }
        requestQueue.add(jsonArrayRequest)

    }

    fun paymentProcess(amount : Int, orderLabel : String, callback: ServerCallback){
        val amountAndOrderLabel = "$amount" + "_" + orderLabel
        val url = "$API_URL$payment/$amountAndOrderLabel"
        Log.d(TAG, "URL: $url")
        Log.d("amountAndOrderLabel", amountAndOrderLabel)
        val postData = JSONObject().apply {
            put("amountAndOrderLabel", amountAndOrderLabel)
        }
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, postData,
            Response.Listener { response ->
                callback.onSuccess(response)
            },
            Response.ErrorListener { error ->
                callback.onError(error)
                Log.e(TAG, "Error: ${error.toString()}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }

        requestQueue.add(jsonObjectRequest)
    }
    interface ServerCallback {
        fun onSuccess(response: JSONObject)
        fun onError(error: VolleyError)
    }
    interface ServerCallbackArray {
        fun onSuccess(response: JSONArray)
        fun onError(error: VolleyError)
    }
}