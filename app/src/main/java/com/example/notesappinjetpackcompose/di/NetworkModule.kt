package com.example.notesappinjetpackcompose.di

import com.example.notesappinjetpackcompose.data.network.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Moshi is just like Gson which is used to convert json into java/kotlin objects and vice versa.

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providesApiService(
        moshi: Moshi        // Inorder to make API call, first we need to check whether we have moshi object or not.
    ): ApiService = Retrofit.Builder()
        .run {
            baseUrl(ApiService.BASE_URL)
            addConverterFactory(MoshiConverterFactory.create(moshi))
            build()
        }.create(ApiService::class.java)
}