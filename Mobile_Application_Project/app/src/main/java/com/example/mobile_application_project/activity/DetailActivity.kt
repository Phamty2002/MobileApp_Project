package com.example.mobile_application_project.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_project.Adapter.ColorAdapter
import com.example.mobile_application_project.Adapter.SizeAdapter
import com.example.mobile_application_project.Adapter.SliderAdapter
import com.example.mobile_application_project.Helper.ManagmentCart
import com.example.mobile_application_project.Model.ItemsModel
import com.example.mobile_application_project.Model.SliderModel
import com.example.mobile_application_project.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private var numberOrder = 1
    private lateinit var managementCart: ManagmentCart
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        getBundle()
        banners()
        initLists()

        binding.favBtn.setOnClickListener {
            addToWishlist()
        }
    }

    private fun initLists() {
        val sizeList = ArrayList<String>()
        for (size in item.size) {
            sizeList.add(size)
        }

        binding.sizeList.adapter = SizeAdapter(sizeList)
        binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val colorList = ArrayList<String>()
        for (imageUrl in item.picUrl) {
            colorList.add(imageUrl)
        }

        binding.colorList.adapter = ColorAdapter(colorList)
        binding.colorList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun banners() {
        val sliderItems = ArrayList<SliderModel>()
        for (imageUrl in item.picUrl) {
            sliderItems.add(SliderModel(imageUrl))
        }

        binding.slider.adapter = SliderAdapter(sliderItems, binding.slider)
        binding.slider.clipToPadding = false
        binding.slider.clipChildren = false
        binding.slider.offscreenPageLimit = 3
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        if (sliderItems.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.slider)
        }
    }

    private fun getBundle() {
        item = intent.getParcelableExtra("object")!!

        binding.titleTxt.text = item.title
        binding.descriptionTxt.text = item.description
        binding.priceTxt.text = "$" + item.price
        binding.ratingTxt.text = "${item.rating} Rating"
        binding.addToCartBtn.setOnClickListener {
            item.numberInCart = numberOrder
            managementCart.insertFood(item)
        }
        binding.backBtn.setOnClickListener { finish() }
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity, CartActivity::class.java))
        }
    }

    private fun addToWishlist() {
        val user = auth.currentUser
        val userEmail = user?.email?.replace(".", ",")
        if (userEmail != null) {
            val wishlistRef = database.child("Wishlists").child(userEmail).push()
            item.orderItemId = wishlistRef.key.toString()
            wishlistRef.setValue(item).addOnSuccessListener {
                Log.d("DetailActivity", "Item added to wishlist: ${item.title}")
                Toast.makeText(this, "Item added to wishlist", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Log.e("DetailActivity", "Failed to add item to wishlist: ${e.message}")
                Toast.makeText(this, "Failed to add item to wishlist", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("DetailActivity", "User email is null")
            Toast.makeText(this, "User email is null", Toast.LENGTH_SHORT).show()
        }
    }
}
