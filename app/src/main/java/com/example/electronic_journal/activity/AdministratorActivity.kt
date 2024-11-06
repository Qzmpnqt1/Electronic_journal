package com.example.electronic_journal.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.electronic_journal.databinding.ActivityAdministratorBinding
import com.example.electronic_journal.server.WebServerSingleton
import com.example.electronic_journal.server.model.Group
import com.example.electronic_journal.server.model.Subject
import com.example.electronic_journal.adapter.SubjectAdapter
import com.example.electronic_journal.server.autorization.GroupDTO
import com.example.electronic_journal.server.autorization.SubjectDTO
import com.example.electronic_journal.server.model.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdministratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdministratorBinding
    private lateinit var subjectAdapter: SubjectAdapter
    private val selectedSubjects = mutableListOf<Subject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadSubjects()
        loadGroups()

        binding.btLogout.setOnClickListener {
            // Очистка токена из SharedPreferences
            val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove("token") // Удаляем токен
                remove("role") // Удаляем роль
                apply()
            }

            // Переход на экран авторизации
            val intent = Intent(this, AuthorizationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() // Закрываем текущую активность
        }

        // Обработчик для добавления группы
        binding.btAddGroup.setOnClickListener {
            val groupName = binding.edGroupName.text.toString().trim()
            if (groupName.isEmpty()) {
                Toast.makeText(this, "Введите название группы", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedSubjects.isEmpty()) {
                Toast.makeText(this, "Выберите хотя бы один предмет", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val subjectIds = selectedSubjects.map { it.subjectId }
            val groupDTO = GroupDTO(
                name = groupName,
                subjectIds = subjectIds
            )

            WebServerSingleton.getApiService(this).addGroup(groupDTO).enqueue(object : Callback<Group> {
                override fun onResponse(call: Call<Group>, response: Response<Group>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdministratorActivity, "Группа добавлена", Toast.LENGTH_SHORT).show()
                        loadGroups()
                    } else {
                        Toast.makeText(this@AdministratorActivity, "Ошибка добавления группы", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Group>, t: Throwable) {
                    Toast.makeText(this@AdministratorActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Обработчик для удаления группы
        binding.btDeleteGroup.setOnClickListener {
            val groupId = binding.edDeleteGroupName.text.toString().trim()
            if (groupId.isEmpty()) {
                Toast.makeText(this, "Введите ID группы для удаления", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            WebServerSingleton.getApiService(this).deleteGroup(groupId.toInt()).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdministratorActivity, "Группа успешно удалена", Toast.LENGTH_SHORT).show()
                        loadGroups()
                    } else if (response.code() == 400) {
                        Toast.makeText(this@AdministratorActivity, "Невозможно удалить группу, в ней есть студенты", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AdministratorActivity, "Ошибка удаления группы", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AdministratorActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                }
            })
    }

        // Обработчик для добавления предмета
        binding.btAddSubject.setOnClickListener {
            val subjectName = binding.edSubjectName.text.toString().trim()
            val subjectCourse = binding.edSubjectCourse.text.toString().trim()

            if (subjectName.isEmpty() || subjectCourse.isEmpty()) {
                Toast.makeText(this, "Введите название и курс предмета", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val course = subjectCourse.toIntOrNull()
            if (course == null || course !in 1..4) {
                Toast.makeText(this, "Курс должен быть числом от 1 до 4", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val subjectDTO = SubjectDTO(name = subjectName, course = course)
            WebServerSingleton.getApiService(this).addSubject(subjectDTO).enqueue(object : Callback<Subject> {
                override fun onResponse(call: Call<Subject>, response: Response<Subject>) {
                    if (response.isSuccessful) {
                        val addedSubject = response.body()
                        if (addedSubject != null) {
                            Toast.makeText(this@AdministratorActivity, "Предмет добавлен: ${addedSubject.name}", Toast.LENGTH_SHORT).show()
                            loadSubjects()
                        }
                    } else if (response.code() == 400) {
                        val errorMessage = response.errorBody()?.string()
                        Toast.makeText(this@AdministratorActivity, errorMessage ?: "Ошибка добавления предмета", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AdministratorActivity, "Ошибка добавления предмета", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Subject>, t: Throwable) {
                    Toast.makeText(this@AdministratorActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Обработчик для удаления предмета
        binding.btDeleteSubject.setOnClickListener {
            val subjectId = binding.edDeleteSubjectName.text.toString().trim()
            if (subjectId.isEmpty()) {
                Toast.makeText(this, "Введите ID предмета для удаления", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            WebServerSingleton.getApiService(this).deleteSubject(subjectId.toInt()).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdministratorActivity, "Предмет удалён", Toast.LENGTH_SHORT).show()
                        loadSubjects()
                    } else {
                        Toast.makeText(this@AdministratorActivity, "Ошибка удаления предмета", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AdministratorActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Загрузка студентов из группы
        binding.btLoadStudents.setOnClickListener {
            val groupId = binding.edGroupIdForStudents.text.toString().trim()
            if (groupId.isEmpty()) {
                Toast.makeText(this, "Введите ID группы", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            WebServerSingleton.getApiService(this).getStudentsByGroupId(groupId.toInt()).enqueue(object : Callback<List<Student>> {
                override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                    if (response.isSuccessful) {
                        val students = response.body()
                        if (students != null && students.isNotEmpty()) {
                            // Сортировка студентов по фамилии в алфавитном порядке
                            val sortedStudents = students.sortedBy { it.surname }

                            // Формируем строку с данными студентов
                            val studentDetails = sortedStudents.joinToString("\n") { "ID: ${it.studentId}, ${it.surname} ${it.name}" }
                            binding.tvStudentList.text = studentDetails
                        } else {
                            binding.tvStudentList.text = "Нет студентов в этой группе"
                        }
                    } else {
                        Toast.makeText(this@AdministratorActivity, "Ошибка загрузки студентов", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                    Toast.makeText(this@AdministratorActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Удаление студента из группы
        binding.btDeleteStudent.setOnClickListener {
            val studentId = binding.edStudentIdToDelete.text.toString().trim()
            if (studentId.isEmpty()) {
                Toast.makeText(this, "Введите ID студента для удаления", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            WebServerSingleton.getApiService(this).removeStudentFromGroup(studentId.toInt()).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdministratorActivity, "Студент удалён из группы", Toast.LENGTH_SHORT).show()
                        // Можно повторно загрузить студентов после удаления
                        binding.btLoadStudents.performClick()
                    } else {
                        Toast.makeText(this@AdministratorActivity, "Ошибка удаления студента", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AdministratorActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private fun loadSubjects() {
        WebServerSingleton.getApiService(this).getAllSubjects().enqueue(object : Callback<List<Subject>> {
            override fun onResponse(call: Call<List<Subject>>, response: Response<List<Subject>>) {
                if (response.isSuccessful) {
                    val subjects = response.body()
                    if (subjects != null) {
                        // Обновление RecyclerView
                        subjectAdapter.updateSubjects(subjects)

                        // Заполнение текстового поля (например, для проверки)
                        val subjectDetails = subjects.joinToString("\n") { "ID: ${it.subjectId}, Название: ${it.name}, Курс: ${it.course}" }
                        binding.tvSubjectDetails.text = subjectDetails
                    } else {
                        binding.tvSubjectDetails.text = "Нет доступных предметов"
                    }
                } else {
                    Log.e("loadSubjects", "Ошибка: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@AdministratorActivity, "Ошибка загрузки предметов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Subject>>, t: Throwable) {
                Log.e("loadSubjects", "Ошибка соединения: ${t.message}")
                Toast.makeText(this@AdministratorActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
            }
        })
    }

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
                        binding.tvGroupList.text = groupDetails.toString()
                    } else {
                        binding.tvGroupDetails.text = "Нет доступных групп"
                        binding.tvGroupList.text = "Нет доступных групп"
                    }
                } else {
                    Log.e("loadGroups", "Ошибка: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@AdministratorActivity, "Ошибка загрузки групп", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Log.e("loadGroups", "Ошибка соединения: ${t.message}")
                Toast.makeText(this@AdministratorActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
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
            layoutManager = LinearLayoutManager(this@AdministratorActivity)
            adapter = subjectAdapter
        }
    }
}