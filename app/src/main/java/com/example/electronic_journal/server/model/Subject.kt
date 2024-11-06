package com.example.electronic_journal.server.model

import com.google.gson.annotations.SerializedName;
import kotlinx.serialization.Serializable

@Serializable
data class Subject (
    @SerializedName("subject_id")
    var subject_id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("teacher_id")
    var teacher_id: String,

    @SerializedName("course")
    var course: Int
)