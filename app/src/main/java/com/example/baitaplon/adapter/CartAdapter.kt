package com.example.baitaplon.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.baitaplon.R
import com.example.baitaplon.data.Product
import com.squareup.picasso.Picasso

class CartAdapter(private val context: Context, private val products: List<Product>) : BaseAdapter() {

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
        Picasso.get().load(product.imageLink).into(viewHolder.productImage)
        return view
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
    }
}