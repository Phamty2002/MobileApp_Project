package com.example.mobile_application_project.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobile_application_project.Model.ItemsModel
import com.example.mobile_application_project.databinding.ActivityCheckoutBinding
import com.example.mobile_application_project.model.Order
import com.example.mobile_application_project.model.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private var total: Double = 0.0
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var cartItems: List<ItemsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        total = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        cartItems = intent.getParcelableArrayListExtra<ItemsModel>("CART_ITEMS") ?: emptyList()

        generateQRCode(total.toString())

        binding.confirmPaymentButton.setOnClickListener {
            validateAndConfirmPayment()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun generateQRCode(text: String) {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
                }
            }
            binding.qrCodeImageView.setImageBitmap(bmp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validateAndConfirmPayment() {
        val paymentAmount = binding.paymentAmountEditText.text.toString().toDoubleOrNull()
        val enteredBankAccount = binding.bankAccountEditText.text.toString()

        if (paymentAmount == null || enteredBankAccount.isEmpty()) {
            Toast.makeText(this, "Please enter valid bank account and payment amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (paymentAmount != total) {
            Toast.makeText(this, "Payment amount must match the total amount", Toast.LENGTH_SHORT).show()
            return
        }

        saveOrderToFirebase()
    }

    private fun saveOrderToFirebase() {
        val user = auth.currentUser
        val orderId = database.child("Orders").push().key
        val orderItems = getOrderItemsFromCart()

        val order = Order(
            orderId = orderId,
            userId = user?.uid,
            items = orderItems,
            totalPrice = total,
            orderTime = System.currentTimeMillis().toString(),
            status = "confirmed"
        )

        orderId?.let {
            database.child("Orders").child(it).setValue(order).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getOrderItemsFromCart(): List<OrderItem> {
        return cartItems.map { item ->
            OrderItem(
                itemId = item.title, // Replace with a unique item ID if available
                quantity = item.numberInCart,
                price = item.price
            )
        }
    }
}
