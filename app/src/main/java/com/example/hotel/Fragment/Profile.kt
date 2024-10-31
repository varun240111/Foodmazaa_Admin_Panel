package com.example.hotel.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hotel.R
import com.example.hotel.databinding.FragmentProfileBinding
import com.example.hotel.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : Fragment() {
    private val auth= FirebaseAuth.getInstance()
    private val database= FirebaseDatabase.getInstance()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater,container,false)

        setUserData()
        binding.saveinfobutton.setOnClickListener{
            val name=binding.name.text.toString()
            val email=binding.email.text.toString()
            val address=binding.address.text.toString()
            val phone=binding.phone.text.toString()
            updateUserData(name,email,address,phone)
        }
        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId=auth.currentUser?.uid
        if(userId!=null){
            val userReference=database.getReference("user").child(userId)
            val userData= hashMapOf("name" to name ,"email" to email,"address" to address,"phone" to phone)
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener{
                    Toast.makeText(requireContext(),"Failed to update profile",Toast.LENGTH_SHORT).show()
                }


        }
     }

    private fun setUserData() {
        val userId=auth.currentUser?.uid
        if(userId!=null){
            val userReference=database.getReference("users").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userprofile=snapshot.getValue(UserModel::class.java)
                        if(userprofile!=null){
                            binding.name.setText(userprofile.username)
                            binding.address.setText(userprofile.address)
                            binding.email.setText(userprofile.email)
                            binding.phone .setText(userprofile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}