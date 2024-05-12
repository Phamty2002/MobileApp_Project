package com.example.mobile_application_project.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_application_project.Adapter.CartAdapter
import com.example.mobile_application_project.Helper.ManagmentCart
import com.example.mobile_application_project.R
import com.example.mobile_application_project.databinding.ActivityCartBinding
import com.example.project1762.Helper.ChangeNumberItemsListener

class CartActivity: BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagmentCart
    private var tax:Double=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart= ManagmentCart(this)

        setVariable()
        initCartList()
        calculateCart()
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun initCartList() {
        binding.viewCart.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.viewCart.adapter=CartAdapter(managementCart.getListCart(),this,object :ChangeNumberItemsListener{
            override fun onChanged() {
                calculateCart()

            }
        })


        with(binding) {
            emptyTxt.visibility=if(managementCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility=if (managementCart.getListCart().isEmpty())View.GONE else View.VISIBLE
        }

    }

    private fun calculateCart() {
        var percentTax=0.02
        var delivery=10.0
        tax = Math.round((managementCart.getTotalFee() * percentTax) * 100 ) / 100.0
        val total=Math.round((managementCart.getTotalFee()+tax+delivery) * 100) / 100
        val itemTotal=Math.round(managementCart.getTotalFee()*100) / 100.0

        with(binding) {
            totalFeeTxt.text="$$itemTotal"
            taxTxt.text="$$tax"
            deliveryTxt.text="$$delivery"
            totalTxt.text="$$total"
        }


    }


}