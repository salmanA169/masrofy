package com.masrofy.di

import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.coroutine.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class  DiModuleActivityScope {

    @Binds
    @ActivityRetainedScoped
   abstract fun provideDispatcher(
        dispatcherProvider:DispatcherProviderImpl
    ):DispatcherProvider

}