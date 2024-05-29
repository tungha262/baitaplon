package com.example.baitaplon.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.baitaplon.R
import com.example.baitaplon.activity.ShoppingActivity
import com.example.baitaplon.databinding.FragmentLoginBinding
import com.example.baitaplon.util.Resource
import com.example.baitaplon.viewmodel.LoginViewmodel
import dagger.hilt.android.AndroidEntryPoint
import android.text.Html
import android.util.Log
import com.android.volley.VolleyError
import com.example.baitaplon.Server.ServerService
import com.example.baitaplon.data.User
import com.example.baitaplon.productController.UserManager
import org.json.JSONObject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewmodel>()
    private var email: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val formattedText = "Bạn chưa có tài khoản? Hãy <u><b>đăng ký</b></u>"
        binding.tvDontHaveAcc.text = Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY)
        binding.tvDontHaveAcc.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            buttonLoginLogin.setOnClickListener {
                email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                viewModel.login(email, password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when(it) {
                    is Resource.Loading -> {
                        binding.buttonLoginLogin.startAnimation()
                    }
                    is Resource.Success -> {
                        getUserDataByEmail(email)
                        binding.buttonLoginLogin.revertAnimation()
                        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.buttonLoginLogin.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }

        }
    fun getUserDataByEmail(email: String) {
        // Gọi hàm truy vấn từ server để lấy thông tin người dùng
        val serverService = ServerService(requireContext())
        serverService.getAccountByEmail(email, object : ServerService.ServerCallback{
            override fun onSuccess(response: JSONObject) {
                val firstName = response.getString("firstName")
                val lastName = response.getString("lastName")
                val email = response.getString("email")
                UserManager.setCurrentUser(User(firstName, lastName, email))
                Log.d("Ok", "Alo Alo")
            }
            override fun onError(error: VolleyError) {
                Log.e("Ngu", "Khong get duoc data")
            }
        })
    }
}
