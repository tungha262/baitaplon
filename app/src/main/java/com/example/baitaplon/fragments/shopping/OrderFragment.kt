package com.example.baitaplon.fragments.shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.VolleyError
import com.example.baitaplon.R
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.adapter.OrderAdapter
import com.example.baitaplon.data.OrderItem
import com.example.baitaplon.productController.UserManager
import org.json.JSONArray
import org.json.JSONException

class OrderFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var emptyOrdersTextView: TextView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var userManager: UserManager
    private lateinit var serverService: ServerService
    private val orderItemList = mutableListOf<OrderItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        userManager = UserManager
        serverService = ServerService(requireContext())
        // Tìm các view trong layout fragment_order.xml
        listView = view.findViewById(R.id.rv_all_orders)
        emptyOrdersTextView = view.findViewById(R.id.tv_empty_orders)
        val closeOrdersImageView = view.findViewById<ImageView>(R.id.imageCloseOrders)

        // Tạo adapter với danh sách đơn hàng
        orderAdapter = OrderAdapter(requireActivity(), orderItemList)
        listView.adapter = orderAdapter

        // Tải dữ liệu đơn hàng từ server
        Log.d("email", userManager.getCurrentUser()!!.email)
        loadOrderData(userManager.getCurrentUser()!!.email)

        closeOrdersImageView.setOnClickListener {
            requireActivity().onBackPressed() // Quay lại màn hình trước đó
        }


        return view
    }

    private fun loadOrderData(email: String) {
        Log.d("Alo?????", "WTF")
        serverService.getOrderByEmail(email, object : ServerService.ServerCallbackArray{
            override fun onSuccess(response: JSONArray) {
                try{
                    orderItemList.clear()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val orderLabel = jsonObject.getString("orderLabel")
                        val totalAmount = jsonObject.getInt("totalAmount")
                        var status = "Đang xử lý"
                        if (jsonObject.getString("token").isNotEmpty()){
                            status = "Thành công"
                        }

                        val orderItem = OrderItem(orderLabel, totalAmount, status)
                        orderItemList.add(orderItem)
                        Log.d("OrderFragment", "Order item: ${orderItemList.size}")
                    }
                }
                catch (e: JSONException){
                    Log.d("Error", e.toString())
                    e.printStackTrace()
                }

                if (orderItemList.isEmpty()) {
                    Log.d("OrderFragment", "Empty")
                    // Nếu danh sách đơn hàng rỗng, hiển thị thông báo "Không có đơn hàng nào"
                    emptyOrdersTextView.visibility = View.VISIBLE
                    listView.visibility = View.GONE
                } else {
                    Log.d("OrderFragment", "Not Empty")
                    // Nếu có đơn hàng, hiển thị danh sách và ẩn thông báo
                    emptyOrdersTextView.visibility = View.GONE
                    listView.visibility = View.VISIBLE
                    // Thông báo cho adapter biết dữ liệu đã thay đổi
                    orderAdapter.notifyDataSetChanged()
                }
            }

            override fun onError(error: VolleyError) {
                Log.e("OrderFragment", error.toString())
            }

        })


    }

}
