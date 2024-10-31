package com.example.hotel.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotel.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartAdapter(
    private val context: Context,
    private val cartItemss: MutableList<String>,
    private val cartItemprices: MutableList<String>,
    private val cartimages: MutableList<String>,
    private val cartdescription: MutableList<String>,
    private val cartQuality: MutableList<Int>,
    private val cartIngredent: MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private var itemQuantities: IntArray = IntArray(cartItemss.size) { 1 }
    private lateinit var cartItemReference: DatabaseReference

    init {
        if (auth.currentUser != null) {
            val database = FirebaseDatabase.getInstance()
            val userId = auth.currentUser?.uid ?: ""
            cartItemReference = database.reference.child("user").child(userId).child("CartItems")
        } else {
            Log.e("CartAdapter", "User is not logged in")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItemss.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        if (position in cartItemss.indices) {
            holder.bind(position)
        } else {
            Log.e("CartAdapter", "Invalid position: $position")
        }
    }

    fun getUpdatedItemsQuantities(): List<Int> {
        return cartQuality.toList()
    }

    inner class CartViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            if (position !in cartItemss.indices || position >= itemQuantities.size) {
                Log.e("CartAdapter", "Invalid position for binding: $position")
                return
            }

            binding.apply {
                val quantity = itemQuantities[position]
                cartfoodname.text = cartItemss[position]
                cartitemprice.text = cartItemprices[position]

                val uriString = cartimages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartimage)

                cartItemQuantity.text = quantity.toString()
                pulsbutton.setOnClickListener { increaseQuantity(position) }
                minusbutton.setOnClickListener { decreaseQuantity(position) }
                deleteButton.setOnClickListener { deleteItem(position) }
            }
        }

        private fun increaseQuantity(position: Int) {
            if (position in itemQuantities.indices && itemQuantities[position] < 10) {
                itemQuantities[position]++
                cartQuality[position]=itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (position in itemQuantities.indices && itemQuantities[position] > 1) {
                itemQuantities[position]--
                cartQuality[position]=itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            getUniqueKeyAtPosition(position) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                } else {
                    Toast.makeText(context, "Failed to find item key", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                cartItemss.removeAt(position)
                cartimages.removeAt(position)
                cartdescription.removeAt(position)
                cartQuality.removeAt(position)
                cartItemprices.removeAt(position)
                cartIngredent.removeAt(position)

                Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()

                itemQuantities = itemQuantities.filterIndexed { index, _ -> index != position }.toIntArray()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartItemss.size)
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
            }
        }
//        fun getUpdatedItemsQuantities(): List<Int> {
//            return cartQuality.toList()
//        }

        private fun getUniqueKeyAtPosition(position: Int, onComplete: (String?) -> Unit) {
            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uniqueKey = snapshot.children.elementAtOrNull(position)?.key
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("CartAdapter", "Database error: ${error.message}")
                }
            })
        }
    }
}
