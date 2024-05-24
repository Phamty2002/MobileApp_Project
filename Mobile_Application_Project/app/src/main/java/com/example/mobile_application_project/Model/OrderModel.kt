package com.example.mobile_application_project.model

data class Order(
    var orderId: String? = null,
    var userId: String? = null,
    var userName: String? = null, // Add userName
    var items: List<OrderItem>? = null,
    var totalPrice: Double = 0.0,
    var orderTime: String? = null,
    var status: String? = null
) {
    constructor() : this(null, null, null, null, 0.0, null, null)
}
