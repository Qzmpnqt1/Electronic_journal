package com.example.electronic_journal.server.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Gradebook (
    @SerializedName("gradebook_id")
    var gradebookId: Int,

    @SerializedName("student_id")
    var studentId: Int
)
