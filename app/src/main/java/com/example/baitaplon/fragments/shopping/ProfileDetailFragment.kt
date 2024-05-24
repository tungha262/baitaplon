package com.example.baitaplon.fragments.shopping

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.baitaplon.R
import com.example.baitaplon.data.User
import com.example.baitaplon.databinding.FragmentProfileDetailBinding


class ProfileDetailFragment : Fragment() {

    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getParcelable<User>("user")

        user?.let {
            Log.d("ProfileDetailFragment", "User: $it")
            // Hiển thị thông tin người dùng lên giao diện
            binding.tvFirstName.text = it.firstName
            binding.tvLastName.text = it.lastName
            binding.tvEmail.text = it.email
        }

        val backButton = view.findViewById<ImageView>(R.id.backButton)

        // Thiết lập OnClickListener cho ImageView
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}