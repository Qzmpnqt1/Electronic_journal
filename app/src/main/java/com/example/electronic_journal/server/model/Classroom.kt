package com.example.electronic_journal.server.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Classroom (
    @SerializedName("classroom_id")
    var classroom_id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("capacity")
    var capacity: Int
)