package com.example.hotel.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.hotel.MenuBottomBlankFragment
import com.example.hotel.R
import com.example.hotel.adpater.PopularAdpater
import com.example.hotel.databinding.FragmentHomeBinding
import com.example.hotel.model.Menultemm
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var menuItems: MutableList<Menultemm>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.ViewAllmenu.setOnClickListener {
            val bottomSheet = MenuBottomBlankFragment()
            bottomSheet.show(parentFragmentManager, "MenuBottomSheet")
        }

        // Retrieve and display popular items from Firebase
       retrieveAndDisplayPopularItems()

        return binding.root
    }

    private fun retrieveAndDisplayPopularItems() {
        database = FirebaseDatabase.getInstance().reference
        val foodRef = database.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()

                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(Menultemm::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }

                }

                // Display random popular items
                randomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun randomPopularItems() {
        if (menuItems.isEmpty()) {
            Toast.makeText(requireContext(), "No popular items to display.", Toast.LENGTH_SHORT).show()
            return
        }
        val index=menuItems.indices.toList().shuffled()
        val numItemsToShow = 6
        val subsetMenuItems = menuItems.shuffled().take(numItemsToShow)
        setPopularItems(subsetMenuItems)
    }

    private fun setPopularItems(subsetMenuItems: List<Menultemm>) {
        val adapter = com.example.hotel.adapter.MenuAdapter(subsetMenuItems, requireContext())
        binding.PopularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopularRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = arrayListOf(
            SlideModel(R.drawable.foodd, ScaleTypes.FIT),
            SlideModel(R.drawable.food2, ScaleTypes.FIT),
            SlideModel(R.drawable.food3, ScaleTypes.FIT)
        )
        val imageSlider=binding.imageSlider
        imageSlider.setImageList(imageList  )
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                // Optional double-click handler
            }

            override fun onItemSelected(position: Int) {
                val itemPosition=imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
