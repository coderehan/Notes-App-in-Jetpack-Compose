package com.example.notesappinjetpackcompose.features.notes.ui.viewmodels.events

import com.example.notesappinjetpackcompose.data.model.NotesRequest

sealed class NotesEvent {

    data class AddNoteEvent(val notesRequest: NotesRequest) : NotesEvent()
    object GetNoteEvent : NotesEvent()
    data class DeleteNoteEvent(val id: Int) : NotesEvent()
    data class UpdateNoteEvent(val id: Int, val notesRequest: NotesRequest) : NotesEvent()

}
