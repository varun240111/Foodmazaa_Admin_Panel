package com.example.hotel.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotel.MainActivity
import com.example.hotel.Pay_out
import com.example.hotel.model.CartItems
import com.example.hotel.adapter.CartAdapter
import com.example.hotel.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cart : Fragment() { // Renamed to follow Kotlin naming conventions
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImageUris: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var foodQuantities: MutableList<Int>
    private lateinit var cartadapter: CartAdapter
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        userId = auth.currentUser?.uid ?: ""
        if (userId.isEmpty()) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return binding.root
        }

        retrieveCartItems()

        binding.processedButton.setOnClickListener {
            //get order item details before procedding to check out
            getOrderItemDetail()
        }

        return binding.root
    }

    private fun getOrderItemDetail() {
        val orderIdReference: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodImageUri = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()

        // Get item quantities from the adapter

            // Get updated quantities from the adapter instance
            val foodQuantities = cartadapter.getUpdatedItemsQuantities()

            orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (foodSnapshot in snapshot.children) {
                        val orderItem = foodSnapshot.getValue(CartItems::class.java)
                        orderItem?.foodName?.let { foodName.add(it) }
                        orderItem?.foodPrice?.let { foodPrice.add(it) }
                        orderItem?.foodDescription?.let { foodDescription.add(it) }
                        orderItem?.foodImage?.let { foodImageUri.add(it) }
                        orderItem?.foodIngredient?.let { foodIngredient.add(it) }
                    }
                    orderNow(foodName, foodPrice, foodDescription, foodImageUri, foodIngredient, foodQuantities)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Order making Failed. Please try again", Toast.LENGTH_SHORT).show()
                }
            })
        }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImageUri: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantities: Any
    ) {
        if(isAdded&& context!=null){
            val intent = Intent(requireContext(), Pay_out ::class.java)
            intent.putExtra("foodItemName",foodName as ArrayList<String>)
            intent.putExtra("foodItemPrice",foodPrice as ArrayList<String>)
            intent.putExtra("foodItemDescription",foodDescription as ArrayList<String>)
            intent.putExtra("foodItemImage",foodImageUri as ArrayList<String>)
            intent.putExtra("foodItemIngredient",foodIngredient as ArrayList<String>)
            intent.putExtra("foodItemQuantity",foodQuantities as ArrayList<Int>)
            startActivity(intent)

        }
    }

    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        val foodReference: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")

        // Initialize lists
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImageUris = mutableListOf()
        foodIngredients = mutableListOf()
        foodQuantities = mutableListOf()

        // Fetch data from the database
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(requireContext(), "No items in the cart", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                for (foodSnapshot in snapshot.children) {
                    val cartItem = foodSnapshot.getValue(CartItems::class.java)

                    // Add cart items to lists
                    cartItem?.foodName?.let { foodNames.add(it) }
                    cartItem?.foodPrice?.let { foodPrices.add(it) }
                    cartItem?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItem?.foodImage?.let { foodImageUris.add(it) }
                    cartItem?.foodIngredient?.let { foodIngredients.add(it) }

                    cartItem?.foodQuantity?.let {
                        // Try converting to an integer safely
                        val quantity = it.toIntOrNull() ?: 1 // Default to 1 if conversion fails
                        foodQuantities.add(quantity)
                    }

                }

                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Failed to fetch data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setAdapter() {
        cartadapter= CartAdapter(
            requireContext(),
            foodDescriptions,
            foodImageUris,
            foodIngredients,
            foodNames,
            foodQuantities,
            foodPrices
        )
        binding.cartRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@cart.cartadapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Int.toIntOrNull(): Int? {
        return try {
            this.toInt()  // Try to convert the string to an integer
        } catch (e: NumberFormatException) {
            null  // Return null if the conversion fails
        }

    }
}