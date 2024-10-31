package com.example.hotel.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotel.databinding.BuyAgainItemBinding
import com.example.hotel.databinding.MenuItemBinding

class Buy_again_adpater(private val buyAgainFoodName:ArrayList<String>, private val buyAgainFoodPrice: ArrayList<String>, private val buyAgainFoodimage:ArrayList<Int>):RecyclerView
.Adapter<Buy_again_adpater.BuyAgainViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding=BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int =buyAgainFoodName.size
        class BuyAgainViewHolder(private val binding:BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root){
                fun bind(foodName: String, foodPrice: String, foodImage: Int) {
                    binding.buyAgainFoodName.text=foodName
                    binding.buyAgainFoodPrice.text=foodPrice
                    binding.buyAgainFoodImage.setImageResource(foodImage)
            }
    }

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainFoodimage[position])
    }

}