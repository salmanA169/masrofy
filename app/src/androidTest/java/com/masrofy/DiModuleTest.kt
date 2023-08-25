package com.masrofy

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.database.RoomCallback
import com.masrofy.utils.Constants
import com.masrofy.utils.datastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.createTestCoroutineScope
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [com.masrofy.di.DiModule::class]
)
object DiModule {
    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context,
        roomCallback: RoomCallback
    ): MasrofyDatabase {
        println("init room ")
        return Room.inMemoryDatabaseBuilder(context, MasrofyDatabase::class.java)
            .addCallback(roomCallback).build()
    }

    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context: Context) :DataStore<Preferences>{
        println("init data store ")
        return PreferenceDataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            produceFile = {
                context.preferencesDataStoreFile("test")
            }
        )
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: MasrofyDatabase) = db.categoryDao
}