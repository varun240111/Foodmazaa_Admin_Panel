package com.example.hotel.model

class Menultemm {
    var foodName: String? = null
    var foodPrice: String? = null
    var foodDescription: String? = null
    var foodIngredient: String? = null
    var foodImage: String? = null

    // No-argument constructor (required by Firebase)
    constructor()

    // Parameterized constructor (optional, for convenience)
    constructor(
        foodName: String?,
        foodPrice: String?,
        foodDescription: String?,
        foodIngredient: String?,
        foodImage: String?
    ) {
        this.foodName = foodName
        this.foodPrice = foodPrice
        this.foodDescription = foodDescription
        this.foodIngredient = foodIngredient
        this.foodImage = foodImage
    }
}
