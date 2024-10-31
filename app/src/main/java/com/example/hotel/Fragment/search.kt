package com.example.hotel.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotel.R
import com.example.hotel.adapter.MenuAdapter
import com.example.hotel.databinding.FragmentSearchBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class search : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter:MenuAdapter
    private val originalMenuFoodName = listOf("Burger", "Sandwich", "Momo", "Masala\nPapad", "Panipuri", "Rice", "Paneer", "Burger", "Sandwich", "Momo", "Masala\nPapad", "Panipuri", "Rice", "Paneer")
    private val originalmenuprice = listOf("$5", "$7", "$8", "$9", "$10", "$11", "$12", "$5", "$7", "$8", "$9", "$10", "$11", "$12")
    private val originalmenuimage = listOf(
        R.drawable.th1,
        R.drawable.th2,
        R.drawable.th3,
        R.drawable.th4,
        R.drawable.th5,
        R.drawable.th7,
        R.drawable.th8,
        R.drawable.th1,
        R.drawable.th2,
        R.drawable.th3,
        R.drawable.th4,
        R.drawable.th5,
        R.drawable.th7,
        R.drawable.th8
    )

    private val filterMenuFoodname = mutableListOf<String>()
    private val filterMenuItemprice = mutableListOf<String>()
    private val filterMenuItemImage = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Initialize the adapter with empty filtered lists
        //adapter = MenuAdapter(filterMenuFoodname, filterMenuItemprice, filterMenuItemImage, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        // Setup for search view
        setupSearchView()
        // Show all menu items
        showAllmenu()

        return binding.root
    }


    private fun showAllmenu() {
        filterMenuFoodname.clear()
        filterMenuItemprice.clear()
        filterMenuItemImage.clear()

        filterMenuFoodname.addAll(originalMenuFoodName)
        filterMenuItemprice.addAll(originalmenuprice)
        filterMenuItemImage.addAll(originalmenuimage)

        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
            val searchView = binding.searchView as androidx.appcompat.widget.SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    filterMenuItems(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterMenuItems(newText)
                    return true
                }
            })

    }

    private fun filterMenuItems(query: String?) {
        filterMenuFoodname.clear()
        filterMenuItemprice.clear()
        filterMenuItemImage.clear()

        // If query is null or empty, show all menu items
        if (query.isNullOrEmpty()) {
            showAllmenu()
            return
        }

        // Log the query to debug
        Log.d("SearchFragment", "Filtering items with query: $query")

        // Perform the filtering
        originalMenuFoodName.forEachIndexed { index, foodName ->
            if (foodName.contains(query, ignoreCase = true)) {
                filterMenuFoodname.add(foodName)
                filterMenuItemprice.add(originalmenuprice[index])
                filterMenuItemImage.add(originalmenuimage[index])
            }
        }

        // Log the filtered results
        Log.d("SearchFragment", "Filtered items: $filterMenuFoodname")

        adapter.notifyDataSetChanged()
    }


}
