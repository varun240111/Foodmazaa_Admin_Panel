package com.example.hotel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotel.adapter.NotificationAdapter
import com.example.hotel.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList

class NotificationBottom : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBottomBinding.inflate(inflater, container, false)
        val notifications= listOf("Your order has been canceled sucessfully",
        "Order Has Been Taken By The Owner ",
        "Congrats Your Order Placed ")
        val notificationImage= listOf(R.drawable.sad,R.drawable.truck,R.drawable.th7)
        val adapter=NotificationAdapter(
            ArrayList(notifications),ArrayList(notificationImage))

        binding.notificationRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter=adapter
        return binding.root
    }

    companion object {

    }
}
