package com.example.mobile_application_project.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mobile_application_project.Adapter.BrandAdapter
import com.example.mobile_application_project.Model.SliderModel
import com.example.mobile_application_project.Adapter.SliderAdapter
import com.example.mobile_application_project.Model.BrandModel
import com.example.mobile_application_project.ViewModel.MainViewModel
import com.example.mobile_application_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel=MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       initBanner()

        initBrand()

    }

    private fun initBanner() {
        binding.progressBarBanner.visibility= View.VISIBLE
        viewModel.banners.observe(this , Observer { items->
            banners(items)
            binding.progressBarBanner.visibility=View.GONE
        })
        viewModel.loadBanners()
    }
    private fun banners(images:List<SliderModel>){
        binding.viewpaperSlider.adapter= SliderAdapter(images,binding.viewpaperSlider)
        binding.viewpaperSlider.clipToPadding=false
        binding.viewpaperSlider.clipChildren=false
        binding.viewpaperSlider.offscreenPageLimit=3
        binding.viewpaperSlider.getChildAt(0 ).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer=CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40 ))
        }
        binding.viewpaperSlider.setPageTransformer(compositePageTransformer)
        if(images.size>1){
            binding.dotIndicator.visibility=View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpaperSlider)
        }
    }

    private fun initBrand() {
        binding.progressBarBanner.visibility=View.VISIBLE
        viewModel.brands.observe(this, Observer{
            binding.viewBrand.layoutManager=LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter=BrandAdapter(it)
            binding.progressBarBanner.visibility=View.GONE
        })
        viewModel.loadBrand()
    }
}