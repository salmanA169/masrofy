package com.masrofy.di

import android.app.Activity
import android.content.Context
import com.masrofy.AdsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object DiAdsModule {

    @ActivityRetainedScoped
    @Provides
    fun provideAdsManager(@ApplicationContext activity:Context)= AdsManager(activity)
}