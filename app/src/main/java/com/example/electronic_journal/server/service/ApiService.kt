package com.example.electronic_journal.server.service

import com.example.electronic_journal.server.autorization.AuthRequest
import com.example.electronic_journal.server.autorization.AuthResponse
import com.example.electronic_journal.server.autorization.GradeEntryRequest
import com.example.electronic_journal.server.autorization.GradebookDTO
import com.example.electronic_journal.server.autorization.GroupDTO
import com.example.electronic_journal.server.autorization.StudentRegistrationRequest
import com.example.electronic_journal.server.autorization.SubjectDTO
import com.example.electronic_journal.server.autorization.TeacherSignUpRequest
import com.example.electronic_journal.server.model.GradeEntry
import com.example.electronic_journal.server.model.Gradebook
import com.example.electronic_journal.server.model.Group
import com.example.electronic_journal.server.model.Student
import com.example.electronic_journal.server.model.Subject
import com.example.electronic_journal.server.model.Teacher
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("auth/register/student")
    fun registerStudent(@Body request: StudentRegistrationRequest): Call<Void>

    @POST("auth/register/teacher")
    fun registerTeacher(@Body request: TeacherSignUpRequest): Call<Void>

    @GET("student/personal-data")
    fun getPersonalDataStudent(): Call<Student>

    @GET("student/gradebook")
    fun getGradebook(): Call<GradebookDTO>

    @GET("gradebook/{gradebookId}/entries")
    fun getGradeEntries(@Path("gradebookId") gradebookId: Int): Call<List<GradeEntry>>  // Получаем оценки для зачетки

    @GET("teacher/personal-data")
    fun getPersonalDataTeacher(): Call<Teacher>

    @GET("teacher/subjects/{subjectId}/groups")
    fun getGroupsForSubject(@Path("subjectId") subjectId: Int): Call<List<Group>>

    @GET("teacher/groups/{groupId}/students")
    fun getStudentsByGroupIdFromTeacher(@Path("groupId") groupId: Int): Call<List<Student>>

    @POST("gradebook/add-grade")
    fun addGrade(@Body gradeEntryRequest: GradeEntryRequest): Call<GradeEntry>

    // Работа с группами
    @POST("admin/groups")
    fun addGroup(@Body groupDTO: GroupDTO): Call<Group>

    @DELETE("admin/groups/{id}")
    fun deleteGroup(@Path("id") groupId: Int): Call<Void>

    @GET("admin/groups")
    fun getAllGroups(): Call<List<Group>>

    // Работа с предметами (в контексте администратора)
    @POST("admin/subjects")
    fun addSubject(@Body subjectDTO: SubjectDTO): Call<Subject>

    @DELETE("admin/subjects/{id}")
    fun deleteSubject(@Path("id") subjectId: Int): Call<Void>

    @GET("admin/subjects")
    fun getAllSubjects(): Call<List<Subject>>

    @GET("admin/groups/{groupId}/students")
    fun getStudentsByGroupId(@Path("groupId") groupId: Int): Call<List<Student>>

    @DELETE("admin/students/{id}")
    fun removeStudentFromGroup(@Path("id") studentId: Int): Call<Void>
}
