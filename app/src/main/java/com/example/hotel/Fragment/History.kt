package com.example.hotel.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotel.R
import com.example.hotel.adpater.Buy_again_adpater
import com.example.hotel.databinding.FragmentHistoryBinding
import com.example.hotel.databinding.FragmentSearchBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [History.newInstance] factory method to
 * create an instance of this fragment.
 */
class History : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdpater: Buy_again_adpater
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        setuprecyclerView()
        return binding.root
    }
        private fun setuprecyclerView(){
           val buyAgainFoodName= arrayListOf("Burger", "Sandwich", "Momo")
            val buyAgainFoodPrice= arrayListOf("$5", "$7", "$8")
            val buyAgainFoodimage= arrayListOf(R.drawable.th1,
                R.drawable.th2,
                R.drawable.th3,)
            buyAgainAdpater=Buy_again_adpater(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodimage)
            binding.BuyAgainRecyclerView.adapter=buyAgainAdpater
            binding.BuyAgainRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            History().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}