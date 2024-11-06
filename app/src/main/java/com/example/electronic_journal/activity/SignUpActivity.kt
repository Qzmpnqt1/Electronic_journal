package com.example.electronic_journal.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.electronic_journal.R
import com.example.electronic_journal.adapter.SubjectAdapter
import com.example.electronic_journal.databinding.ActivitySignUpBinding
import com.example.electronic_journal.server.WebServerSingleton
import com.example.electronic_journal.server.autorization.StudentRegistrationRequest
import com.example.electronic_journal.server.autorization.TeacherSignUpRequest
import com.example.electronic_journal.server.model.Group
import com.example.electronic_journal.server.model.Subject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var subjectAdapter: SubjectAdapter
    private val selectedSubjects = mutableListOf<Subject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSubjects.visibility = View.GONE
        binding.rvSubjects.visibility = View.GONE
        binding.edDateOfBirth.visibility = View.VISIBLE
        binding.tvGroupDetails.visibility = View.VISIBLE
        binding.edGroupId.visibility = View.VISIBLE

        // Загрузка всех групп и предметов
        setupRecyclerView()
        loadSubjects()
        loadGroups()

        // Обработчик нажатия на кнопку регистрации
        binding.btSignUp.setOnClickListener {
            val name = binding.edName.text.toString().trim()
            val surname = binding.edSurname.text.toString().trim()
            val patronymic = binding.edPatronymic.text.toString().trim()
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            // Валидация полей
            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when {
                binding.rbStudent.isChecked -> {
                    // Для студентов нужно указать только имя, фамилию, отчество, дату рождения, ID группы, email и пароль
                    val dateOfBirth = binding.edDateOfBirth.text.toString().trim()
                    val groupIdText = binding.edGroupId.text.toString().trim()

                    if (dateOfBirth.isEmpty() || groupIdText.isEmpty()) {
                        Toast.makeText(this, "Пожалуйста, заполните все поля для студента", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val groupId = groupIdText.toIntOrNull()
                    if (groupId == null) {
                        Toast.makeText(this, "ID группы должно быть числом", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val request = StudentRegistrationRequest(
                        name = name,
                        surname = surname,
                        patronymic = patronymic,
                        dateOfBirth = dateOfBirth,
                        email = email,
                        password = password,
                        groupId = groupId
                    )

                    WebServerSingleton.getApiService(this).registerStudent(request)
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@SignUpActivity, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@SignUpActivity, AuthorizationActivity::class.java))
                                    finish()
                                } else {
                                    val errorMessage = response.errorBody()?.string()
                                    Toast.makeText(this@SignUpActivity, "Ошибка регистрации: $errorMessage", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(this@SignUpActivity, "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                }

                binding.rbTeacher.isChecked -> {
                    // Для учителей нужно указать только имя, фамилию, отчество, предметы, email и пароль
                    if (selectedSubjects.isEmpty()) {
                        Toast.makeText(this, "Пожалуйста, выберите хотя бы один предмет", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val subjectIds = selectedSubjects.map { it.subjectId }
                    val request = TeacherSignUpRequest(
                        name = name,
                        surname = surname,
                        patronymic = patronymic,
                        email = email,
                        password = password,
                        subjectIds = subjectIds
                    )

                    WebServerSingleton.getApiService(this).registerTeacher(request)
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@SignUpActivity, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@SignUpActivity, AuthorizationActivity::class.java))
                                    finish()
                                } else {
                                    val errorMessage = response.errorBody()?.string()
                                    Toast.makeText(this@SignUpActivity, "Ошибка регистрации: $errorMessage", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(this@SignUpActivity, "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }
        }

        // Переход к экрану авторизации
        binding.txAutorization.setOnClickListener {
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Устанавливаем слушатель на RadioGroup
        binding.rgUserType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbStudent -> {
                    // При выборе "Ученик" скрываем элементы для учителей
                    binding.tvSubjects.visibility = View.GONE
                    binding.rvSubjects.visibility = View.GONE

                    // Показываем элементы для студента
                    binding.edDateOfBirth.visibility = View.VISIBLE
                    binding.tvGroupDetails.visibility = View.VISIBLE
                    binding.edGroupId.visibility = View.VISIBLE
                }
                R.id.rbTeacher -> {
                    // При выборе "Учитель" скрываем элементы для студентов
                    binding.edDateOfBirth.visibility = View.GONE
                    binding.tvGroupDetails.visibility = View.GONE
                    binding.edGroupId.visibility = View.GONE

                    // Показываем элементы для учителя
                    binding.tvSubjects.visibility = View.VISIBLE
                    binding.rvSubjects.visibility = View.VISIBLE
                }
            }
        }
    }

    // Метод для подгрузки всех групп в TextView
    private fun loadGroups() {
        WebServerSingleton.getApiService(this).getAllGroups().enqueue(object : Callback<List<Group>> {
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                if (response.isSuccessful) {
                    val groups = response.body()
                    if (groups != null) {
                        // Сортировка групп по названию в алфавитном порядке
                        val sortedGroups = groups.sortedBy { it.name }

                        val groupDetails = StringBuilder()
                        for (group in sortedGroups) {
                            groupDetails.append("ID: ${group.groupId}, Название: ${group.name}\n")
                        }

                        // Установка отсортированного текста в TextView
                        binding.tvGroupDetails.text = groupDetails.toString()
                    } else {
                        binding.tvGroupDetails.text = "Нет доступных групп"
                    }
                } else {
                    Log.e("loadGroups", "Ошибка: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@SignUpActivity, "Ошибка загрузки групп", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Log.e("loadGroups", "Ошибка соединения: ${t.message}")
                Toast.makeText(this@SignUpActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadSubjects() {
        WebServerSingleton.getApiService(this).getAllSubjects().enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                if (response.isSuccessful) {
                    val subjects = response.body()
                    if (subjects != null) {
                        subjectAdapter.updateSubjects(subjects)
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "Ошибка загрузки предметов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        subjectAdapter = SubjectAdapter(mutableListOf()) { subject, isSelected ->
            if (isSelected) {
                selectedSubjects.add(subject)
            } else {
                selectedSubjects.remove(subject)
            }
        }
        binding.rvSubjects.apply {
            layoutManager = LinearLayoutManager(this@SignUpActivity)
            adapter = subjectAdapter
        }
    }
}

