package com.example.electronic_journal.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.electronic_journal.databinding.ActivityAuthorizationBinding
import com.example.electronic_journal.server.WebServerSingleton
import com.example.electronic_journal.server.autorization.AuthRequest
import com.example.electronic_journal.server.autorization.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val role = sharedPref.getString("role", null)

        if (token != null && role != null) {
            redirectToRoleActivity(role)
            finish()
        }

        binding.txSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btLogin.setOnClickListener {
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val authRequest = AuthRequest(email, password)
            val call = WebServerSingleton.getApiService(this).login(authRequest)
            Log.d("AuthorizationActivity", "JSON для отправки: $authRequest")
            call.enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        val authResponse = response.body()
                        if (authResponse != null) {
                            val token = authResponse.token
                            val role = authResponse.role

                            with(sharedPref.edit()) {
                                putString("token", token)
                                putString("role", role)
                                apply()
                            }
                            redirectToRoleActivity(role)
                            finish()
                        } else {
                            Toast.makeText(this@AuthorizationActivity, "Ошибка обработки ответа", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AuthorizationActivity, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    // Добавьте логирование ошибки
                    Log.e("AuthorizationActivity", "Ошибка соединения", t)
                    Toast.makeText(this@AuthorizationActivity, "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun redirectToRoleActivity(role: String) {
        when (role) {
            "ROLE_ADMIN" -> startActivity(Intent(this, AdministratorActivity::class.java))
            "ROLE_TEACHER" -> startActivity(Intent(this, TeacherActivity::class.java))
            "ROLE_STUDENT" -> startActivity(Intent(this, StudentActivity::class.java))
            else -> Toast.makeText(this, "Неизвестная роль", Toast.LENGTH_SHORT).show()
        }
    }
}
