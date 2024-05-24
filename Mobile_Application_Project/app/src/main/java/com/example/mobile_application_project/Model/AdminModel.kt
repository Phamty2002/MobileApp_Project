package com.example.mobile_application_project.model

data class AdminModel(
    var adminId: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var phone: String? = null,
    var address: String? = null
) {
    constructor() : this(null, null, null, null, null, null)
}
