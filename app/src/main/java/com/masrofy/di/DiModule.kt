package com.masrofy.di

import android.content.Context
import androidx.room.Room
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.database.RoomCallback
import com.masrofy.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {
    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context,
        roomCallback: RoomCallback
    ) = Room.databaseBuilder(context, MasrofyDatabase::class.java, Constants.DATABASE_NAME)
        .addCallback(roomCallback).build()
}