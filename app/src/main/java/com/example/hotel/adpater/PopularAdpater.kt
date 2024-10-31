package com.example.hotel.adpater

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotel.DetailActivity
import com.example.hotel.databinding.PopularItemBinding

class PopularAdpater(private  val items:List<String>,private val price:List<String>,private val image:List<Int>,private val requireContext: Context): RecyclerView.Adapter<PopularAdpater.PopularViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularAdpater.PopularViewHolder {
        return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
       val item=items[position]
        val images=image[position]
        val price=price[position]
        holder.bind(item,price,images)
        holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, DetailActivity::class.java)
                intent.putExtra("MenuName", item)
                intent.putExtra("MenuImage", images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class PopularViewHolder(private val binding:PopularItemBinding) :RecyclerView.ViewHolder(binding.root){
        private val imagesView=binding.imageView6
        fun bind(item:String,price: String,images: Int) {

            binding.FoodPopular.text=item
            binding.Pricepopular.text=price
            imagesView.setImageResource(images)
        }

    }

}