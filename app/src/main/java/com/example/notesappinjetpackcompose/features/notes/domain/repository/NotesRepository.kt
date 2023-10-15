package com.example.notesappinjetpackcompose.features.notes.domain.repository

import com.example.notesappinjetpackcompose.data.model.NotesRequest
import com.example.notesappinjetpackcompose.data.model.NotesResponse
import kotlinx.coroutines.flow.Flow

// We have to implement this domain layer interface repository in data layer repository

interface NotesRepository {

    suspend fun addNotes(notesRequest: NotesRequest): Flow<NotesResponse>

    suspend fun getNotes(): Flow<List<NotesResponse>>

    suspend fun deleteNotes(id: Int): Flow<NotesResponse>

    suspend fun updateNotes(id: Int, notesRequest: NotesRequest): Flow<NotesResponse>

}