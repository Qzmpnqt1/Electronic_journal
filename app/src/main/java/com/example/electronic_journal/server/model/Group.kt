package com.example.electronic_journal.server.model

import com.google.gson.annotations.SerializedName;
import kotlinx.serialization.Serializable;

@Serializable
data class Group (
    @SerializedName("group_id")
    var group_id: Int,

    @SerializedName("name")
    var name: String
)