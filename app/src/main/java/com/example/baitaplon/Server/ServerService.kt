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



    interface ServerCallback {
        fun onSuccess(response: JSONObject)
        fun onError(error: VolleyError)
    }
    interface ServerCallbackArray {
        fun onSuccess(response: JSONArray)
        fun onError(error: VolleyError)
    }
}