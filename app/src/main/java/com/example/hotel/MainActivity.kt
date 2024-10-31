package com.example.hotel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hotel.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController: NavController = findNavController(R.id.fragmentContainerView)
        val bottomNav: BottomNavigationView = binding.bottomNavigationView
        bottomNav.setupWithNavController(navController)

        binding.notificationButton.setOnClickListener {
            val bottomSheetDialg =NotificationBottom()
            bottomSheetDialg.show(supportFragmentManager, "Test")
        }

    }
}
