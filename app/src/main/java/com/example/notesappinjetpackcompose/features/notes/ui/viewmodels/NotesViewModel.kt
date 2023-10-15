package com.example.notesappinjetpackcompose.features.notes.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesappinjetpackcompose.data.model.NotesResponse
import com.example.notesappinjetpackcompose.features.notes.domain.repository.NotesRepository
import com.example.notesappinjetpackcompose.features.notes.ui.viewmodels.events.NotesEvent
import com.example.notesappinjetpackcompose.features.notes.ui.viewmodels.states.NotesState
import com.example.notesappinjetpackcompose.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository) :
    ViewModel() {

    private val addNoteEventMutableSharedFlow = MutableSharedFlow<ApiState<NotesResponse>>()      // private MutableSharedFlow
    val addNoteEventSharedFlow = addNoteEventMutableSharedFlow.asSharedFlow()                     // public SharedFlow

    private val deleteNoteEventMutableSharedFlow = MutableSharedFlow<ApiState<NotesResponse>>()      // private MutableSharedFlow
    val deleteNoteEventSharedFlow = deleteNoteEventMutableSharedFlow.asSharedFlow()                 // public SharedFlow

    private val updateNoteEventMutableSharedFlow = MutableSharedFlow<ApiState<NotesResponse>>()      // private MutableSharedFlow
    val updateNoteEventSharedFlow = updateNoteEventMutableSharedFlow.asSharedFlow()                 // public SharedFlow

    private val getNoteEventMutableStateFlow = mutableStateOf(NotesState())
    val getNoteEventStateFlow: State<NotesState> = getNoteEventMutableStateFlow

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.AddNoteEvent -> {
                viewModelScope.launch {
                    notesRepository.addNotes(event.notesRequest)
                        .onStart {
                            addNoteEventMutableSharedFlow.emit(ApiState.Loading)
                        }.catch {
                            addNoteEventMutableSharedFlow.emit(
                                ApiState.Failure(
                                    it.message ?: "Something went wrong"
                                )
                            )
                        }.collect {
                            addNoteEventMutableSharedFlow.emit(ApiState.Success(it))
                        }

                }
            }

            is NotesEvent.DeleteNoteEvent -> {
                viewModelScope.launch {
                    notesRepository.deleteNotes(event.id)
                        .onStart {
                            deleteNoteEventMutableSharedFlow.emit(ApiState.Loading)
                        }.catch {
                            deleteNoteEventMutableSharedFlow.emit(
                                ApiState.Failure(
                                    it.message ?: "Something went wrong"
                                )
                            )
                        }.collect {
                            deleteNoteEventMutableSharedFlow.emit(ApiState.Success(it))
                        }

                }
            }


            is NotesEvent.UpdateNoteEvent -> {
                viewModelScope.launch {
                    notesRepository.updateNotes(event.id, event.notesRequest)
                        .onStart {
                            updateNoteEventMutableSharedFlow.emit(ApiState.Loading)
                        }.catch {
                            updateNoteEventMutableSharedFlow.emit(
                                ApiState.Failure(
                                    it.message ?: "Something went wrong"
                                )
                            )
                        }.collect {
                            updateNoteEventMutableSharedFlow.emit(ApiState.Success(it))
                        }

                }
            }

            is NotesEvent.GetNoteEvent -> {
                viewModelScope.launch {
                    notesRepository.getNotes()
                        .onStart {
                            getNoteEventMutableStateFlow.value =
                                NotesState(
                                    isLoading = true
                                )
                        }.catch {
                            getNoteEventMutableStateFlow.value =
                                NotesState(
                                    error = it.message ?: "Something went wrong"
                                )
                        }.collect {
                            getNoteEventMutableStateFlow.value =
                                NotesState(
                                    data = it
                                )
                        }

                }
            }
        }
    }

}