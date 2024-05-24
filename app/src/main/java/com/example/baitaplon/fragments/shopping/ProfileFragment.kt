package com.example.baitaplon.fragments.shopping

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.baitaplon.R
import com.example.baitaplon.activity.LoginRegisterActivity
import com.example.baitaplon.databinding.FragmentProfileBinding
import com.example.baitaplon.productController.UserManager

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mContext: Context
    private lateinit var userManager: UserManager
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logout.setOnClickListener {
            val intent = Intent(mContext, LoginRegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            userManager.clearCurrentUser()
        }

        binding.constraintProfile.setOnClickListener {

            val user = UserManager.getCurrentUser()
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            findNavController().navigate(R.id.action_profileFragment_to_profileDetailFragment, bundle)
        }
    }
}
