package com.example.notesappinjetpackcompose.data.model

data class NotesResponse(
    val id: Int,
    val title: String,
    val description: String,
    val created_at: String,
    val updated_at: String
)
