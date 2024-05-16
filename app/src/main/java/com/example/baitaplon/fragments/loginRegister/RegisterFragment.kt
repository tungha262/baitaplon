package com.example.baitaplon.fragments.loginRegister
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.baitaplon.data.User
import com.example.baitaplon.util.Resource
import com.google.android.material.button.MaterialButton

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
    }
}


