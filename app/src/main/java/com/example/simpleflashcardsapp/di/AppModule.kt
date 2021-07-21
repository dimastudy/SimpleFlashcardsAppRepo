package com.example.simpleflashcardsapp.di

import android.app.Application
import android.content.Context
import com.example.simpleflashcardsapp.database.getDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context:Context) =
        getDatabase(context)

}