package com.example.electronic_journal.server.model

import com.google.gson.annotations.SerializedName;
import kotlinx.serialization.Serializable

@Serializable
data class Teacher (
    @SerializedName("teacher_id")
    var teacher_id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("surname")
    val surname: String,

    @SerializedName("patronymic")
    val patronymic: String? = null,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)