package com.example.notesappinjetpackcompose.data.network

import com.example.notesappinjetpackcompose.data.model.NotesRequest
import com.example.notesappinjetpackcompose.data.model.NotesResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    companion object {
        const val BASE_URL = "http://18.235.233.99/api/"
    }

    @GET("todo")
    suspend fun getNotes(): List<NotesResponse>

    @POST("todo/")
    suspend fun addNotes(@Body notesRequest: NotesRequest): NotesResponse

    @DELETE("todo/{id}/")
    suspend fun deleteNotes(@Path("id") id: Int): NotesResponse

    @PUT("todo/{id}/")
    suspend fun updateNotes(@Path("id") id: Int, @Body notesRequest: NotesRequest): NotesResponse

}