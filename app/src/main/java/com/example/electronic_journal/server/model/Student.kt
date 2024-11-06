package com.example.electronic_journal.server.model

import com.google.gson.annotations.SerializedName;
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Student (
    @SerializedName("student_id")
    val student_id: Int,

    @SerializedName("group_id")
    val group_id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("surname")
    val surname: String,

    @SerializedName("patronymic")
    val patronymic: String? = null,

    @SerializedName("date_of_birth")
    val date_of_birth: LocalDate? = null,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)