package com.example.electronic_journal.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.electronic_journal.R
import com.example.electronic_journal.databinding.ActivityStudentBinding
import com.example.electronic_journal.fragment.student.GradebookFragment
import com.example.electronic_journal.fragment.student.PersonalDataStudentFragment

class StudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        // Устанавливаем начальный фрагмент
        if (savedInstanceState == null) {
            loadFragment(PersonalDataStudentFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            Log.d("StudentActivity", "Selected item: ${menuItem.itemId}")
            when (menuItem.itemId) {
                R.id.nav_personal_data -> {
                    loadFragment(PersonalDataStudentFragment())
                    true
                }
                R.id.nav_gradebook -> {
                    loadFragment(GradebookFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        Log.d("StudentActivity", "Loading fragment: ${fragment::class.java.simpleName}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

