package com.example.hotel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotel.adapter.MenuAdapter
import com.example.hotel.databinding.FragmentMenuBottomBlankBinding
import com.example.hotel.model.Menultemm
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBottomBlankFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBottomBlankBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<Menultemm>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomBlankBinding.inflate(inflater, container, false)

        binding.buttonback.setOnClickListener {
            dismiss()
        }

        retrieveMenuItems()

        return binding.root
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val itemRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val menuItem = itemSnapshot.getValue(Menultemm::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                Log.d("ITEMS", "onDataChange: Data Received")
                // Once data received, set through adapter
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ITEMS", "onCancelled: ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        if (menuItems.isNotEmpty()) {
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.AllmenuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.AllmenuRecyclerView.adapter = adapter
            Log.d("ITEMS", "setAdapter: Data set")
        } else {
            Log.d("ITEMS", "setAdapter: Data not set")
            }
        }
}
