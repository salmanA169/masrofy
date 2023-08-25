package com.masrofy.di

import android.content.Context
import com.masrofy.AdsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object DiAdsModule {

    @ActivityRetainedScoped
    @Provides
    fun provideAdsManager(@ApplicationContext activity:Context)= AdsManager(activity)


}