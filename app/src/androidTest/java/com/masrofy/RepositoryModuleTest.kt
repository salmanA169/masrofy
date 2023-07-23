package com.masrofy

import com.masrofy.repository.TransactionRepository
import com.masrofy.repository.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.testing.TestInstallIn

//@Module
//@TestInstallIn(components = [ActivityComponent::class], replaces = [com.masrofy.di.RepositoryModule::class])
//abstract class RepositoryModule {
//
//    @Binds
//    abstract fun bindTransactionRepository(transactionRepository: TransactionRepositoryImpl): TransactionRepository
//}