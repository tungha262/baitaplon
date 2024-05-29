package com.example.baitaplon.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.baitaplon.R
import com.example.baitaplon.adapter.OrderAdapter
import com.example.baitaplon.data.OrderItem
import org.json.JSONArray
import org.json.JSONException

class OrderFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var emptyOrdersTextView: TextView
    private lateinit var orderAdapter: OrderAdapter
    private val orderItemList = mutableListOf<OrderItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)


        // Tìm các view trong layout fragment_order.xml
        listView = view.findViewById(R.id.rv_all_orders)
        emptyOrdersTextView = view.findViewById(R.id.tv_empty_orders)
        val closeOrdersImageView = view.findViewById<ImageView>(R.id.imageCloseOrders)

        // Tạo adapter với danh sách đơn hàng
        orderAdapter = OrderAdapter(requireActivity(), orderItemList)
        listView.adapter = orderAdapter

        // Tải dữ liệu đơn hàng từ server
        loadOrderData()

        closeOrdersImageView.setOnClickListener {
            requireActivity().onBackPressed() // Quay lại màn hình trước đó
        }


        return view
    }

    // Phương thức tải dữ liệu đơn hàng từ server
    private fun loadOrderData() {
        // Dữ liệu giả
        val jsonResponse = """
            [
                {"productId": "P001", "price": 10.0, "statusOrder": "Delivered"},
                {"productId": "P002", "price": 15.0, "statusOrder": "Processing"}
            ]
        """

        try {
            // Chuyển đổi chuỗi JSON thành JSONArray
            val jsonArray = JSONArray(jsonResponse)

            // Lặp qua từng đối tượng JSON trong mảng
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val productId = jsonObject.getString("productId")
                val price = jsonObject.getDouble("price")
                val statusOrder = jsonObject.getString("statusOrder")

                // Tạo đối tượng OrderItem và thêm vào danh sách
                val orderItem = OrderItem(productId, price, statusOrder)
                orderItemList.add(orderItem)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // Cập nhật giao diện dựa trên dữ liệu đơn hàng
        if (orderItemList.isEmpty()) {
            // Nếu danh sách đơn hàng rỗng, hiển thị thông báo "Không có đơn hàng nào"
            emptyOrdersTextView.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            // Nếu có đơn hàng, hiển thị danh sách và ẩn thông báo
            emptyOrdersTextView.visibility = View.GONE
            listView.visibility = View.VISIBLE
            // Thông báo cho adapter biết dữ liệu đã thay đổi
            orderAdapter.notifyDataSetChanged()
        }
    }
}
