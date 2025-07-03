package com.example.pocketflow.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pocketflow.R
import com.example.pocketflow.databinding.ActivityLoginBinding
import com.example.pocketflow.model.SharedPref

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        sharedPref = SharedPref(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        if (sharedPref.isLogin()) {
            // Sudah login, langsung ke MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // Belum login, tampilkan LoginFragment
            setContentView(view)
        }
    }
}