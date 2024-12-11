package com.example.electronic_journal.server.autorization

import com.google.gson.annotations.SerializedName

data class GroupRequest(
    @SerializedName("name")
    var name: String
)