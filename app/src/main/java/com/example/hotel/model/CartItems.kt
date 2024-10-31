package com.example.hotel.model


data class CartItems(
    val foodName: String? = null,
    val foodPrice: String? = null,
    val foodDescription: String? = null,
    val foodImage: String? = null,
    val foodQuantity: Int? = null,  // Changed to Int? instead of String?
    val foodIngredient: String? = null
)


