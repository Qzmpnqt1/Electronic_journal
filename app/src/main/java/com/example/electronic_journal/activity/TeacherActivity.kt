package com.example.electronic_journal.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.electronic_journal.R
import com.example.electronic_journal.databinding.ActivityTeacherBinding
import com.example.electronic_journal.fragment.teacher.PersonalDataTeacherFragment
import com.example.electronic_journal.fragment.teacher.TeacherSubjectFragment

class TeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_personal_data -> {
                    // Открыть фрагмент с личными данными
                    openFragment(PersonalDataTeacherFragment())
                    true
                }
                R.id.nav_grade -> {
                    // Открыть фрагмент для выставления оценок
                    openFragment(TeacherSubjectFragment())
                    true
                }
                else -> false
            }
        }

        // Загружаем фрагмент по умолчанию
        if (savedInstanceState == null) {
            openFragment(PersonalDataTeacherFragment()) // Начальный фрагмент
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)  // Если нужно, чтобы при нажатии "назад" можно было вернуться
        transaction.commit()
    }
}


