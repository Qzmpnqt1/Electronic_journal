package com.example.electronic_journal.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.electronic_journal.R
import com.example.electronic_journal.databinding.ActivityAuthorizationBinding
import com.example.electronic_journal.databinding.ActivitySignUpBinding

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_authorization)

        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent);
        }
    }
}