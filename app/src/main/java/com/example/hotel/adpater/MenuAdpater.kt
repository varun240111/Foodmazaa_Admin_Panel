package com.example.hotel.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotel.DetailActivity
import com.example.hotel.R
import com.example.hotel.databinding.MenuItemBinding
import com.example.hotel.model.Menultemm

class MenuAdapter(
    private val menuItems: List<Menultemm>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)
                }
            }
        }

        private fun openDetailActivity(position: Int) {
            val menuItem = menuItems[position]
            val intent = Intent(requireContext, DetailActivity::class.java).apply {
                putExtra("foodName", menuItem.foodName)
                putExtra("foodPrice", menuItem.foodPrice)
                putExtra("foodDescription", menuItem.foodDescription)
                putExtra("foodIngredient", menuItem.foodIngredient)
                putExtra("foodImage", menuItem.foodImage)
            }
            requireContext.startActivity(intent)
        }

        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuFoodPopular.text = menuItem.foodName
                menuPrice.text = menuItem.foodPrice

                // Decode Base64 image and set it
                val bitmap = decodeBase64ToBitmap(menuItem.foodImage)
                if (bitmap != null) {
                    menuimage.setImageBitmap(bitmap)
                } else {
                    menuimage.setImageResource(R.drawable.palace_holder) // Use a placeholder image if decoding fails
                }
            }
        }

        private fun decodeBase64ToBitmap(encodedImage: String?): Bitmap? {
            return try {
                if (encodedImage.isNullOrEmpty()) {
                    return null
                }
                val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                null
            }
        }
    }
}
