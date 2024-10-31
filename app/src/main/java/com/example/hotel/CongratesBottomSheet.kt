package com.example.hotel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hotel.databinding.FragmentCongratesBottomSheetBinding

class CongratesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCongratesBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and initialize the binding
        binding = FragmentCongratesBottomSheetBinding.inflate(inflater, container, false)

        // Set up the gohome button listener
        binding.gohome.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root // Return the root view of the binding
    }
}
