package com.example.notesappinjetpackcompose.features.notes.ui.viewmodels.states

import com.example.notesappinjetpackcompose.data.model.NotesResponse

// Whatever the data we get from server api, we will store here
data class NotesState(
    val data: List<NotesResponse> = emptyList(),        // By default, we will keep it empty list until we get data from server
    val error: String = "",                             // By default, we will keep it empty string until we get error message from server
    val isLoading: Boolean = false                      // By default, we will keep it false until we start loading to get data from server
)