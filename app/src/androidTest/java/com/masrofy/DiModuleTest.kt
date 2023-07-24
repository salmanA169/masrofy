package com.masrofy

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
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [com.masrofy.di.DiModule::class])
object DiModule {
    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context,
        roomCallback: RoomCallback
    ):MasrofyDatabase  {
        println("init room ")
        return Room.inMemoryDatabaseBuilder(context, MasrofyDatabase::class.java).addCallback(roomCallback).build()
    }
}