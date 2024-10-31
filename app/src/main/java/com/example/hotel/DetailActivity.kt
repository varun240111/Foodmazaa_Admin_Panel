package com.example.hotel

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hotel.databinding.ActivityDetailBinding
import com.example.hotel.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null
    private var foodImage: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Retrieve intent data
        foodName = intent.getStringExtra("MenuItemName") ?: "Default Food Name"
        foodIngredients = intent.getStringExtra("MenuItemIngredient") ?: "No Ingredients"
        foodPrice = intent.getStringExtra("MenuItemPrice") ?: "Price Not Available"
        foodDescription = intent.getStringExtra("MenuItemDescription") ?: "No Description"
        foodImage = intent.getStringExtra("MenuItemImage") ?: ""

        // Update the views
        populateDetails()

        // Set listeners
        binding.imagebutton.setOnClickListener { finish() }
        binding.addcartbutton.setOnClickListener { addItemToCart() }
    }

    private fun populateDetails() {
        with(binding) {
            detailfoodname.text = foodName
            detailfooddescriton.text = foodDescription
            detailingrediants.text = foodIngredients

            // Load image safely using Glide
            if (!foodImage.isNullOrEmpty()) {
                try {
                    Glide.with(this@DetailActivity)
                        .load(Uri.parse(foodImage))
                        .into(detailfoodimage)
                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } else {
                detailfoodimage.setImageResource(R.drawable.palace_holder) // Use a placeholder if image is missing
            }
        }
    }

    private fun addItemToCart() {
        val userId = auth.currentUser?.uid
        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val cartItem = CartItems(
            foodName = foodName ?: "Unknown Food",
            foodPrice = foodPrice ?: "0",
            foodDescription = foodDescription ?: "No Description",
            foodImage = foodImage ?: "",
            foodQuantity = 1, // Default quantity as String
            foodIngredient = foodIngredients ?: "No Ingredients"
        )

        FirebaseDatabase.getInstance().reference
            .child("user")
            .child(userId)
            .child("CartItems")
            .push()
            .setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Item added to cart successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
            }
    }
}
