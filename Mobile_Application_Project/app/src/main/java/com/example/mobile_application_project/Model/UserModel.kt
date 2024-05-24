package com.example.mobile_application_project.model

data class UserModel(
    var userId: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var role: String? = null // Add this line
) {
    // No-argument constructor required for Firebase
    constructor() : this(null, null, null, null, null, null, null)
}
