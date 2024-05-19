package com.example.baitaplon.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.baitaplon.R
import com.example.baitaplon.databinding.FragmentIntroducetionBinding

class IntroductionFragment : Fragment() {

    private var binding: FragmentIntroducetionBinding? = null
    private val mBinding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroducetionBinding.inflate(inflater, container, false)

        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        mBinding.buttonStart.setOnClickListener {
            // navigate to login screen here
            navController.navigate(R.id.action_introductionFragment_to_accountOptionsFragment, null)
        }
        return mBinding.root


    }
}


