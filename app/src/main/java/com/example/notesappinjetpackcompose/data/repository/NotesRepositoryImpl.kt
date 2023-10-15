package com.example.notesappinjetpackcompose.data.repository

import com.example.notesappinjetpackcompose.data.model.NotesRequest
import com.example.notesappinjetpackcompose.data.model.NotesResponse
import com.example.notesappinjetpackcompose.data.network.ApiService
import com.example.notesappinjetpackcompose.features.notes.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

// Here in this repository, we will be implementing domain layer repository interface
// This repository will talk with api interface

class NotesRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    NotesRepository {

    override suspend fun addNotes(notesRequest: NotesRequest): Flow<NotesResponse> = flow {
        emit(apiService.addNotes(notesRequest))
    }.flowOn(Dispatchers.IO)

    override suspend fun getNotes(): Flow<List<NotesResponse>> = flow {
        emit(apiService.getNotes())
    }

    override suspend fun deleteNotes(id: Int): Flow<NotesResponse> = flow {
        emit(apiService.deleteNotes(id))
    }

    override suspend fun updateNotes(id: Int, notesRequest: NotesRequest): Flow<NotesResponse> = flow {
            emit(apiService.updateNotes(id, notesRequest))
    }


}