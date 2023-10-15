package com.example.notesappinjetpackcompose.di

import com.example.notesappinjetpackcompose.data.repository.NotesRepositoryImpl
import com.example.notesappinjetpackcompose.features.notes.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    // Binds annotation is used for providing repository interface
    @Binds
    abstract fun providesNotesRepository(
        notesRepositoryImpl: NotesRepositoryImpl
    ): NotesRepository

}