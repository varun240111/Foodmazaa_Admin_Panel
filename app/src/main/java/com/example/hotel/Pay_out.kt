package com.example.hotel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hotel.databinding.ActivityPayOutBinding
import com.example.hotel.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Pay_out : AppCompatActivity() {
    lateinit var binding: ActivityPayOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemIngredient: ArrayList<String>
    private lateinit var foodItemQuantities: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference()
        //set user data
        SetUserData()
        val intent=intent
        foodItemName=intent.getStringArrayListExtra("foodItemName") as ArrayList<String>
        foodItemPrice=intent.getStringArrayListExtra("foodItemPrice") as ArrayList<String>
        foodItemImage=intent.getStringArrayListExtra("foodItemImage") as ArrayList<String>
        foodItemDescription=intent.getStringArrayListExtra("foodItemDescription") as ArrayList<String>
        foodItemIngredient=intent.getStringArrayListExtra("foodItemIngredient") as ArrayList<String>
        foodItemQuantities = intent.getIntegerArrayListExtra("foodItemQuantities") as? ArrayList<Int> ?: arrayListOf()
        totalAmount=calculateTotalAmount().toString()+" ₹"
        binding.totalAmount.isEnabled=false
        binding.totalAmount.setText(totalAmount)
        binding.backbutton.setOnClickListener{
            finish()
        }
        binding.placeorder.setOnClickListener {
            //get data from textView
             name=binding.name.text.toString().trim()
            address=binding.address.text.toString().trim()
            phone=binding.phone.text.toString().trim()
            if(name.isEmpty() && address.isEmpty() && phone.isEmpty()){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }
            else{
                placeorder()
            }
        }

    }

    private fun placeorder() {
        userId=auth.currentUser?.uid?:""
        val time=System.currentTimeMillis()
        val itemPushKey=databaseReference.child("OrderDetails").push().key
        val orderDetails=OrderDetails( userId,name,foodItemName,foodItemPrice,foodItemQuantities,foodItemImage,address,totalAmount,phone,false,false,itemPushKey,time)
        val orderReference=databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog=CongratesBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"Test")
            removeItemFromCart()
            finish()
        }
    }

    private fun removeItemFromCart() {
        val cartItemsReference=databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): String {
        var totalAmount = 0
        for (i in 0 until foodItemQuantities.size) {
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == '₹') {
                price.dropLast(1).toInt()
            } else {
                price.toInt()
            }
            val quantity = foodItemQuantities[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount.toString()
    }

    private fun SetUserData() {
        val user=auth.currentUser
        if(user!=null){
            userId=user.uid
            val userReference=databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        val names= snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""
                        binding.apply {
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}