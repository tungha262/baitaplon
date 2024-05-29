package com.example.baitaplon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.baitaplon.R
import com.example.baitaplon.data.OrderItem

// Adapter để hiển thị danh sách các mục đơn hàng trong ListView
class OrderAdapter(context: Context, private val orderItems: List<OrderItem>) : ArrayAdapter<OrderItem>(context, 0, orderItems) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Nếu convertView không tồn tại, inflate nó từ layout order_item.xml
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.order_item, parent, false)

        // Lấy đối tượng OrderItem tại vị trí hiện tại
        val orderItem = getItem(position)

        // ánh xa
        val tvProductCartID = view.findViewById<TextView>(R.id.tvOrderLabel)
        val tvProductCartPrice = view.findViewById<TextView>(R.id.tvTotalAmount)
        val tvProductCartStatusOrder = view.findViewById<TextView>(R.id.tvProductCartStatusOrder)

        // Đặt dữ liệu vào các view tương ứng
        orderItem?.let {
            tvProductCartID.text = "Mã đơn hàng: ${it.orderLabel}"  // Đặt mã sản phẩm
            tvProductCartPrice.text = "Tổng số tiền: ${it.totalAmount.formatPrice()}đ" // Đặt giá sản phẩm trực tiếp
            tvProductCartStatusOrder.text = it.orderStatus
            if (it.orderStatus == "Thành công") {
                tvProductCartStatusOrder.setTextColor(context.getColor(R.color.green))
            }
        }
        return view
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
