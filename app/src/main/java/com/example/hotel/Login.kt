package com.example.hotel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hotel.databinding.ActivityLoginBinding
import com.example.hotel.databinding.ActivitySignBinding
import com.example.hotel.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private var username: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = Firebase.auth
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.loginButton.setOnClickListener{
            email = binding.emailAddress.text.toString().trim()
            password = binding.password12.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else {
                createUser()
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            }
        }

        binding.donthavebutton.setOnClickListener{
            val intent=Intent(this,SignActivity::class.java)
            startActivity(intent)
        }

        binding.googleButtonlog.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account: GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                        task->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                task->
            if(task.isSuccessful) {
                val user = auth.currentUser
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserData()
                        val user = auth.currentUser
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun saveUserData() {
        val email = binding.emailAddress.text.toString().trim()
        val password = binding.password12.text.toString().trim()

        val user = UserModel(username,email, password)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
            database.child("users").child(userId).setValue(user)
        }

    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
