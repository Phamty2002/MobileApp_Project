package com.example.mobile_application_project.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mobile_application_project.Adapter.BrandAdapter
import com.example.mobile_application_project.Adapter.PopularAdapter
import com.example.mobile_application_project.Adapter.SliderAdapter
import com.example.mobile_application_project.Model.SliderModel
import com.example.mobile_application_project.ViewModel.MainViewModel
import com.example.mobile_application_project.databinding.ActivityMainBinding
import com.example.mobile_application_project.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("USER_EMAIL")?.replace(".", ",")
        if (email == null) {
            Log.e("MainActivity", "Email is null")
            finish()
            return
        }

        database = FirebaseDatabase.getInstance().reference.child("Users").child(email)

        // Fetch user data and update UI
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                if (user != null) {
                    binding.textViewUserName.text = user.name ?: "Jaytee"
                } else {
                    Log.e("MainActivity", "User data is null")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Database error: ${error.message}")
            }
        })

        initBanner()
        initBrand()
        initPopular()
        initBottomMenu()

        // Set OnClickListener for the profile button
        binding.profileBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            Log.d("MainActivity", "Navigating to Profile with email: $email")
            intent.putExtra("USER_EMAIL", email) // Pass the email to the profile activity
            startActivity(intent)
        }

        // Set OnClickListener for the order button
        binding.orderBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, OrderActivity::class.java)
            intent.putExtra("USER_EMAIL", email) // Pass the email to the order activity
            startActivity(intent)
        }

        // Set OnClickListener for the wishlist button
        binding.wishlistBtn.setOnClickListener {
            val email = intent.getStringExtra("USER_EMAIL")
            if (email != null) {
                val intent = Intent(this@MainActivity, WishlistActivity::class.java)
                intent.putExtra("USER_EMAIL", email)
                startActivity(intent)
            } else {
                Log.e("MainActivity", "User email is null")
            }
        }
    }

    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, CartActivity::class.java)
            )
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items ->
            banners(items)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewpaperSlider.adapter = SliderAdapter(images, binding.viewpaperSlider)
        binding.viewpaperSlider.clipToPadding = false
        binding.viewpaperSlider.clipChildren = false
        binding.viewpaperSlider.offscreenPageLimit = 3
        binding.viewpaperSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpaperSlider.setPageTransformer(compositePageTransformer)
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpaperSlider)
        }
    }

    private fun initBrand() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer {
            binding.viewBrand.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter = BrandAdapter(it)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBrand()
    }

    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.popular.observe(this, Observer {
            binding.viewPopular.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.viewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadPupolar()
    }
}
