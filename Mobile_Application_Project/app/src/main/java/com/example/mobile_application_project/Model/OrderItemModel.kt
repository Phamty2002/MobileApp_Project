package com.example.mobile_application_project.model

data class OrderItem(
    var itemId: String? = null,
    var title: String? = null, // Use title for item name
    var quantity: Int = 0,
    var price: Double = 0.0
) {
    constructor() : this(null, null, 0, 0.0)
}
