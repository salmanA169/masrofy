package com.masrofy

import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.coroutine.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [com.masrofy.di.DiModuleActivityScope::class])
@Module
abstract class  DiModuleActivityScope {

    @Binds
    @Singleton
    abstract fun provideDispatcher(
        dispatcherProvider: TestDispatcherProvider
    ): DispatcherProvider

}