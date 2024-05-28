package com.example.baitaplon.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.data.Product
import com.squareup.picasso.Picasso
import org.json.JSONObject

class CartAdapter(private val context: Context, private val products: MutableList<Product>) : BaseAdapter() {

    interface CartListener {
        fun onProductRemoved(product: Product)
        fun onProductQuantityChanged(product: Product, quantity: Int)
    }

    private var listener: CartListener? = null

    fun setCartListener(listener: CartListener) {
        this.listener = listener
    }

    override fun getCount(): Int {
        return products.size
    }

    override fun getItem(position: Int): Any {
        return products[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.cart_product_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val product = products[position]
        viewHolder.productName.text = product.productName
        viewHolder.productPrice.text = product.price?.formatPrice()
        viewHolder.productQuantity.text = product.quantity.toString()
        Picasso.get().load(product.imageLink).into(viewHolder.productImage)

        viewHolder.btnRemoveProduct.setOnClickListener {
            removeProduct(product)
        }

        viewHolder.btnAddProduct.setOnClickListener{
            product.quantity = product.quantity!! + 1
            listener?.onProductQuantityChanged(product, product.quantity!!)
        }
        return view
    }

    private fun removeProduct(product: Product) {
        val serverService = ServerService(context)

        product.id?.let {
            serverService.removeProductByID(it, object : ServerService.ServerCallback {
                override fun onSuccess(response: JSONObject) {
                    Log.d("CartAdapter", "Product removed: $response")
                    listener?.onProductRemoved(product)
                    notifyDataSetChanged()
                }

                override fun onError(error: VolleyError) {
                    Log.d("CartAdapter", "Error removing product: $error")
                }
            })
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

    private class ViewHolder(view: View) {
        val productImage: ImageView = view.findViewById(R.id.imageCartProduct)
        val productName: TextView = view.findViewById(R.id.tvProductCartName)
        val productPrice: TextView = view.findViewById(R.id.tvProductCartPrice)
        val productQuantity: TextView = view.findViewById(R.id.tvProductQuantity)
        val btnRemoveProduct: ImageView = view.findViewById(R.id.btn_remove_product)
        val btnAddProduct: ImageView = view.findViewById(R.id.btn_add_product)
    }
}