package com.example.electronic_journal.server.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Gradebook (
    @SerializedName("gradebook_id")
    var gradebook_id: Int,

    @SerializedName("student_id")
    var student_id: Int,

    @SerializedName("subject_id")
    var subject_id: Int,

    @SerializedName("grade")
    var grade: Int,

    @SerializedName("date_of_grade")
    var date_of_grade: LocalDate
)