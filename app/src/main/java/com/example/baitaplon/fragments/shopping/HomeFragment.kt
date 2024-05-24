package com.example.baitaplon.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.baitaplon.R
import com.example.baitaplon.adapter.HomeViewpagerAdapter
import com.example.baitaplon.adapter.ProductAdapter
import com.example.baitaplon.databinding.FragmentHomeBinding
import com.example.baitaplon.fragments.categories.AcerFragment
import com.example.baitaplon.fragments.categories.AsusFragment
import com.example.baitaplon.fragments.categories.DellFragment
import com.example.baitaplon.fragments.categories.MacBookFragment
import com.example.baitaplon.fragments.categories.MainCategoryFragment
import com.example.baitaplon.fragments.categories.MsiFragment
import com.example.baitaplon.productController.ProductManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {
    private  lateinit var  binding : FragmentHomeBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productManager: ProductManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            MacBookFragment(),
            MsiFragment(),
            DellFragment(),
            AsusFragment(),
            AcerFragment()
        )
        val viewPage2Adapter =
            HomeViewpagerAdapter(categoryFragments, childFragmentManager, lifecycle)

        binding.viewpagerHome.adapter= viewPage2Adapter
        TabLayoutMediator(binding.tablayout, binding.viewpagerHome){tab, position ->
            when (position) {
                0 -> tab.text= "All"
                1 -> tab.text= "MacBook"
                2 -> tab.text= "MSI"
                3 -> tab.text= "Dell"
                4 -> tab.text= "Asus"
                5 -> tab.text= "Acer"

            }
        }.attach()
    }
}