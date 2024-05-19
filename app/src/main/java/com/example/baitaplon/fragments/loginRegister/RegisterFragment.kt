package com.example.baitaplon.fragments.loginRegister
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.baitaplon.R
import com.example.baitaplon.databinding.FragmentRegisterBinding
import com.example.baitaplon.viewmodel.RegisterViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.Bidi
import kotlinx.coroutines.launch


import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.baitaplon.data.User
import com.example.baitaplon.util.RegisterValidation
import com.example.baitaplon.util.Resource
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var biding: FragmentRegisterBinding
    private val viewModel : RegisterViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        biding = FragmentRegisterBinding.inflate(inflater)
        return biding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        biding.tvDoHaveAcc.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        biding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user = User(
                    FirstNameRegister.text.toString().trim(),
                    LastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim()
                )
                val password = edPasswordRegister.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        biding.buttonRegisterRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test", it.message.toString())
                        Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        biding.buttonRegisterRegister.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG,it.message.toString())
                        biding.buttonRegisterRegister.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        biding.edEmailRegister.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        biding.edPasswordRegister.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
                if(validation.firstName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main) {
                        biding.FirstNameRegister.apply {
                            requestFocus()
                            error = validation.firstName.message
                        }
                    }
                }
                if(validation.lastName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main) {
                        biding.LastNameRegister.apply {
                            requestFocus()
                            error = validation.lastName.message
                        }
                    }
                }
            }
        }
    }
}


