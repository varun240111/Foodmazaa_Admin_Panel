package com.example.hotel

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hotel.databinding.ActivityChooseLocationBinding
import com.example.hotel.databinding.ActivitySplashScreenBinding

class Choose_location : AppCompatActivity() {
    private val binding: ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val localeList= arrayOf("Yeola","Nashik","Ahilyanagar"," Shirdi","Rahuri")
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,localeList)
        val autoCompleteTextView=binding.ListOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}